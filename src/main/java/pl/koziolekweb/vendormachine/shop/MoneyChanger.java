package pl.koziolekweb.vendormachine.shop;

import org.springframework.stereotype.Component;
import pl.koziolekweb.vendormachine.validators.CoinValidator;

import java.util.LinkedList;
import java.util.List;

@Component
class MoneyChanger {

    private final List<Integer> validCoins = CoinValidator.valid;

    public List<Integer> charge(long total) {
        if (total < 0) {
            return List.of();
        }
        List<Integer> coins = new LinkedList<>();
        while (total > 0) {
            int n = 0;
            for (int i : validCoins) {
                if ((i <= total) && (i > n)) {
                    n = i;
                }
            }
            if(n == 0 ){
                return List.of(); // no coins
            }
            total -= n;
            coins.add(n);
        }
        return coins;
    }
}
