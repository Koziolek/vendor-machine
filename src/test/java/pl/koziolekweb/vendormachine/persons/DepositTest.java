package pl.koziolekweb.vendormachine.persons;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

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
import pl.koziolekweb.vendormachine.products.ProductRepository;
import pl.koziolekweb.vendormachine.security.UserService;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class DepositTest {

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
	ObjectMapper objectMapper = new ObjectMapper();
	@Autowired
	private ProductRepository productRepository;
	private MockMvc mvc;

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
	@WithMockUser(username = "buyer", password = "buyer", roles = {"BUYER"})
	@SneakyThrows
	void shouldDepositMoneyAndThenReset() {
		mvc.perform(
				post("/deposit")
						.content("""
								{
								   "coins": [5, 10, 20, 50, 100]
								}
								""")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(
						result -> {
							var user = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
							assertThat(result.getResponse().getStatus()).isEqualTo(200);
							assertThat(user.getDeposit()).isEqualTo(185L);
						});

		mvc.perform(
				post("/reset")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andDo(
						result -> {
							var user = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);
							assertThat(result.getResponse().getStatus()).isEqualTo(200);
							assertThat(user.getDeposit()).isEqualTo(0L);
						});
	}
}