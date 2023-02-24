package pl.koziolekweb.vendormachine.shop;

import io.jsonwebtoken.io.IOException;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import pl.koziolekweb.vendormachine.persons.User;
import pl.koziolekweb.vendormachine.persons.UserRepository;
import pl.koziolekweb.vendormachine.products.Product;
import pl.koziolekweb.vendormachine.products.ProductRepository;

@Component
@RequiredArgsConstructor
class VendorMachineGuts {
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final MoneyChanger moneyChanger;
	private final PlatformTransactionManager platformTransactionManager;

	public Either<String, Ledger> disposeProduct(BuyProductRequest request, User currentUser) {
		var enoughProducts = new EnoughProductsPredicate(request.amount());
		var enoughMoney = new EnoughMoneyPredicate(request.amount(), currentUser.getDeposit());

		return Try.of(() -> productRepository.findById(UUID.fromString(request.productId()))
				.filter(enoughProducts.and(enoughMoney))
				.map(p -> {
					p.setAmount(p.getAmount() - request.amount());
					return p;
				})
				.map(entity -> {
					TransactionTemplate template = new TransactionTemplate(platformTransactionManager);
					return template.execute((status) -> {
						productRepository.save(entity);
						final long totalCost = request.amount() * entity.getCost();
						final long newDeposit = currentUser.getDeposit() - totalCost;
						currentUser.setDeposit(newDeposit);
						userRepository.save(currentUser);
						return new Ledger(totalCost, moneyChanger.charge(newDeposit));
					});
				})
				.orElseThrow(() -> new IOException("Not enough funds or items"))).toEither()
				.bimap(Throwable::getMessage, Function.identity());
	}
}

record Ledger(long total, List<Integer> deposit) {
}

@RequiredArgsConstructor
class EnoughProductsPredicate implements Predicate<Product> {

	private final long requestedAmount;

	@Override
	public boolean test(Product p) {
		return p.getAmount() > requestedAmount;
	}
}

@RequiredArgsConstructor
class EnoughMoneyPredicate implements Predicate<Product> {

	private final long requestedAmount;
	private final long availableMoney;

	@Override
	public boolean test(Product p) {
		return p.getCost() * requestedAmount < availableMoney;
	}
}
