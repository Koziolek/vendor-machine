package pl.koziolekweb.vendormachine.assertions;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.Assertions;
import pl.koziolekweb.vendormachine.products.Product;

public class ProductAssertion extends AbstractAssert<ProductAssertion, Product> {

    protected ProductAssertion(Product product) {
        super(product, ProductAssertion.class);
    }

    public static ProductAssertion assertThat(Product actual) {
        return new ProductAssertion(actual);
    }

    public ProductAssertion hasName(String name) {
        Assertions.assertThat(actual.getName()).isEqualTo(name);
        return this;
    }

    public ProductAssertion hasCost(int cost) {
        Assertions.assertThat(actual.getCost()).isEqualTo(cost);
        return this;
    }

    public ProductAssertion hasAmount(long amount) {
        Assertions.assertThat(actual.getAmount()).isEqualTo(amount);
        return this;
    }
}
