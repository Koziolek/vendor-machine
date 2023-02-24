package pl.koziolekweb.vendormachine.security;

import pl.koziolekweb.vendormachine.persons.User;

public interface UserService {
	User registerUser(User newUser);
}
