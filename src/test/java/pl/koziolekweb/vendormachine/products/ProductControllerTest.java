package pl.koziolekweb.vendormachine.products;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
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
import pl.koziolekweb.vendormachine.security.UserService;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static pl.koziolekweb.vendormachine.assertions.ProductAssertion.assertThat;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductControllerTest {


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
    @Autowired
    ProductRepository productRepository;

    ObjectMapper objectMapper;

    private MockMvc mvc;

    public ProductControllerTest() {
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
                .deposit(0)
                .build());

        userService.registerUser(User.builder()
                .role(Role.SELLER)
                .username("seller")
                .password("seller")
                .deposit(0)
                .build());
    }

    @AfterEach
    void tearDown() {
        productRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "seller", password = "seller", roles = {"SELLER"})
    @SneakyThrows
    void shouldPutProductIntoMachine() {
        var productId = new LinkedList<UUID>();

        mvc.perform(
                get("/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var products = objectMapper.readValue(result.getResponse().getContentAsString(), Collection.class);
                    assertThat(products).isEmpty();
                }
        );

        mvc.perform(
                post("/product")
                        .content(objectMapper.writeValueAsString(new CreateProductRequest(10L, 10, "product")))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var product = objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);
                    assertThat(product).hasName("product").hasAmount(10).hasCost(10);
                    productId.clear();
                    productId.add(product.getId());
                }
        );

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
                                    .filter(p -> p.getId().equals(productId.getFirst()))
                                    .findFirst()
                    ).isPresent();
                }
        );

        mvc.perform(
                put("/product")
                        .content(objectMapper.writeValueAsString(
                                new UpdateProductRequest(
                                        productId.getFirst(),
                                        5,
                                        5,
                                        "new product"
                                )
                        ))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var product = objectMapper.readValue(result.getResponse().getContentAsString(), Product.class);
                    assertThat(product).hasName("new product").hasAmount(5).hasCost(5);
                }
        );

        mvc.perform(
                get("/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var products = (Collection<?>) objectMapper.readValue(result.getResponse().getContentAsString(), Collection.class);
                    assertThat(products).hasSize(1);
                    assertThat(
                            products.stream()
                                    .map(o -> (Map<String, Object>) o)
                                    .map(m -> m.get("id"))
                                    .filter(
                                            id -> id.equals(productId.getFirst().toString())
                                    )
                                    .findFirst()
                    ).isPresent();
                }
        );

        mvc.perform(
                delete("/product")
                        .content(objectMapper.writeValueAsString(
                                new DeleteProductRequest(productId.getFirst())
                        ))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    assertThat(result.getResponse().getStatus()).isEqualTo(204);
                }
        );

        mvc.perform(
                get("/product")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(
                result -> {
                    var products = objectMapper.readValue(result.getResponse().getContentAsString(), Collection.class);
                    assertThat(products).isEmpty();
                }
        );

    }
}