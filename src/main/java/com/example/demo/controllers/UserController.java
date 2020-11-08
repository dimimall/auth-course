package com.example.demo.controllers;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.UserApplication;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;

import java.io.IOException;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class.getName());

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

//	@Autowired
//	private com.splunk.TcpInput tcpInput;

	@GetMapping("/id/{id}")
	public ResponseEntity<UserApplication> findById(@PathVariable Long id) {
		return ResponseEntity.of(userRepository.findById(id));
	}
	
	@GetMapping("/{username}")
	public ResponseEntity<UserApplication> findByUserName(@PathVariable String username) {
		UserApplication user = userRepository.findByUsername(username);
		if (user != null){
			logger.info("SUCCESS: user is : "+username);

		}
		else {
			logger.error("FAIL: there is no user with that name: "+username);
		}
		return user == null ? ResponseEntity.notFound().build() : ResponseEntity.ok(user);
	}
	
	@PostMapping("/create")
	public ResponseEntity<UserApplication> createUser(@RequestBody CreateUserRequest createUserRequest) {
		UserApplication user = new UserApplication();
		user.setUsername(createUserRequest.getUsername());

		logger.info("SUCCESS: username set with "+createUserRequest.getUsername());
//		try {
//			tcpInput.submit("INFO: New user create request received");
//		} catch (IOException e) {
//			e.printStackTrace();
//			logger.error(e.getLocalizedMessage());
//		}

		Cart cart = new Cart();
		cartRepository.save(cart);
		if (createUserRequest.getPassword().length() < 7 || !createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			logger.error("FAIL: with user password cannot create User() "+ createUserRequest.getUsername());
			return ResponseEntity.badRequest().build();
		}

		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		user.setCart(cart);
		userRepository.save(user);
		return ResponseEntity.ok(user);
	}
	
}
