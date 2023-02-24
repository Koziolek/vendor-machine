package pl.koziolekweb.vendormachine.shop;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.koziolekweb.vendormachine.persons.Role;
import pl.koziolekweb.vendormachine.persons.User;
import pl.koziolekweb.vendormachine.persons.UserRepository;
import pl.koziolekweb.vendormachine.products.Product;
import pl.koziolekweb.vendormachine.products.ProductRepository;
import pl.koziolekweb.vendormachine.security.UserService;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class VendorMachineTest {

    @Autowired
    WebApplicationContext context;
    @Autowired
    FilterChainProxy springSecurityFilterChain;
    @Autowired
    UserService userService;
    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    UserRepository userRepository;
    ObjectMapper objectMapper;
    Product exampleProduct;
    @Autowired
    private ProductRepository productRepository;
    private MockMvc mvc;

    public VendorMachineTest() {
        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(springSecurityFilterChain))
                .build();

        userService.registerUser(User.builder()
                .role(Role.BUYER)
                .username("buyer")
                .password("buyer")
                .deposit(185)
                .build());

        userService.registerUser(User.builder()
                .role(Role.SELLER)
                .username("seller")
                .password("seller")
                .deposit(0)
                .build());

        exampleProduct = productRepository.save(
                Product.builder()
                        .amount(100l)
                        .cost(10)
                        .name("product")
                        .build()

        );
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "buyer", password = "buyer", roles = {"BUYER"})
    @SneakyThrows
    void shouldBuyProduct() {
        mvc.perform(
                        post("/buy")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new BuyProductRequest(exampleProduct.getId().toString(), 5)))
                )
                .andDo(result -> {
                    var response = objectMapper.readValue(result.getResponse().getContentAsString(), BuyProductResponse.class);
                    Assertions.assertThat(response.totalCost()).isEqualTo(50);
                    Assertions.assertThat(response.coins()).containsOnly(100, 20, 10, 5);
                });
        mvc.perform(
                get("/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var products = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Collection<Product>>() {
                    });
                    assertThat(products).hasSize(1);
                    assertThat(
                            products.stream()
                                    .findFirst()
                    ).hasValueSatisfying(p -> assertThat(p.getAmount()).isEqualTo(95L));
                }
        );

    }


    @Test
    @WithMockUser(username = "buyer", password = "buyer", roles = {"BUYER"})
    @SneakyThrows
    void shouldNoBuyProduct() {
        mvc.perform(
                        post("/buy")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(new BuyProductRequest(exampleProduct.getId().toString(), 50)))
                )
                .andDo(result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(418);
                });

        mvc.perform(
                get("/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var products = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<Collection<Product>>() {
                    });
                    assertThat(products).hasSize(1);
                    assertThat(
                            products.stream()
                                    .findFirst()
                    ).hasValueSatisfying(p -> assertThat(p.getAmount()).isEqualTo(100L));
                }
        );
    }


}