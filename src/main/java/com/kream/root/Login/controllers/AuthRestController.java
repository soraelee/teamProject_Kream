package com.kream.root.Login.controllers;


import com.kream.root.Login.jwt.JwtTokenProvider;
import com.kream.root.Login.model.SignInResultDTO;
import com.kream.root.Login.model.SignUpResultDTO;
import com.kream.root.Login.model.TempUserDTO;
import com.kream.root.Login.model.UserListDTO;
import com.kream.root.Login.service.SignService;
import com.kream.root.Login.service.TokenBlacklistService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthRestController {
	
	private final Logger LOGGER = LoggerFactory.getLogger(SwaggerController.class);
	  
	@Autowired
    private SignService signService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;
	
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

		@GetMapping("/accessLogin/{id}")
		public String accessLogin(@PathVariable Long id){
			System.out.println("id = " + id);
			return "OK";
		}

	@PostMapping(value = "/loginCheck")
	public String loginCheck(@RequestBody TempUserDTO dto, HttpServletResponse response) {

		LOGGER.info("[signIn] Attempting to log in. with id : {}", dto.getUserId());
		SignInResultDTO signInResultDTO = signService.signIn( dto.getUserId(),  dto.getUserPw());

		if (signInResultDTO.getCode() == 1) {
			LOGGER.info("[signIn] Successfully logged in. id : {}, token : {}", dto.getUserId(), signInResultDTO.getToken());

			// Get JWT token from signInResultDTO
			String jwtToken = signInResultDTO.getToken();

			Cookie loginCookie= new Cookie("loginCookie", dto.getUserId());
			loginCookie.setPath("/");
			loginCookie.setHttpOnly(true);
			loginCookie.setMaxAge(60 * 60); // 1 hour , cuz jwtToken is 1 hour exp
//         loginCookie.setSecure(true); // 이 속성과
//         loginCookie.setAttribute("SameSite", "None"); // 이 속성 추가

			LOGGER.info("로그인 쿠키",loginCookie);
			System.out.println("loginCookie = " + loginCookie);

			response.addCookie(loginCookie);


			if (jwtTokenProvider.validateToken(jwtToken)) {

				Authentication authentication = jwtTokenProvider.getAuthentication(jwtToken);

				System.out.println("jwtToken:"+jwtToken);
				System.out.println("authentication:"+authentication);

				SecurityContextHolder.getContext().setAuthentication(authentication);

				//response.setHeader("X-AUTH-TOKEN",jwtToken );




				return jwtToken;
			} else {
				LOGGER.info("JWT token validation failed for user id: {}", dto.getUserId());
				;
				return null;
			}
		} else {
			LOGGER.info("Login failed for user id: {}",dto.getUserId());

			return null;
		}
	}
	  
	  
	  
	  @PostMapping(value = "/registerCheck")
	  public SignUpResultDTO registerCheck(@RequestBody UserListDTO userDTO) {
	      String role = "normal"; // admin 만 아니면 어떤 스트링이던지 상관없음
	      System.out.println("userId:" + userDTO.getUserId());

	      // Call the signUp method with all the necessary fields
	      SignUpResultDTO signUpResultDTO = signService.signUp(
	          userDTO.getUserId(),
	          userDTO.getUserPw(),
	          userDTO.getUsername(),
	          role,
	          userDTO.getEmail(),
	          userDTO.getPhone(),
	          userDTO.getAge(),
	          userDTO.getGender()
	      );

	      LOGGER.info("[signUp] 회원가입을 완료했습니다. id : {}", userDTO.getUserId());
	      return signUpResultDTO;
	  }

	  
	  
		@PostMapping("/rddCheck")
	    public int rddUserId(@RequestBody String userIdData) { 
			 System.out.println("rdddddddddddddddddddddd");
	        System.out.println("Data received from client: " + userIdData);
	       
	        int result = 0;
	        try {
	            result = signService.redundancyCheck(userIdData);
	            System.out.println("rddResult:" + result);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return result;
	    }
		
		 @PostMapping("/logout")
		    public int logout(HttpServletResponse response,
					HttpServletRequest request, @RequestHeader("token-for-blacklist") String token) {
			 	int result=0;
		        try {
					System.out.println("token = " + token);

		            if (jwtTokenProvider.validateToken(token)) {// 아직 유효한 토큰이라면 ~~
		                // Blacklist the token
		            	Date tokenExpDate=jwtTokenProvider.getExpirationDate(token);
		            	tokenBlacklistService.blacklistToken(token,tokenExpDate);

						Cookie loginCookie = new Cookie("loginCookie", null);
						loginCookie.setPath("/");
						loginCookie.setHttpOnly(true);
						loginCookie.setMaxAge(0); // Setting max age to 0 to delete the cookie

						response.addCookie(loginCookie);

		                // Clear the security context
		                SecurityContextHolder.clearContext();
		                result=1;
		                return result;
		            } else {
		                return result;
		            }
		        } catch (Exception e) {
		        	LOGGER.info("error while logging out:" + e.getMessage());
		            return result;
		        }
		    }

//	@PostMapping("/verifyToken")
//	public Map<String, Object> verifyToken(@RequestBody Map<String, String> request) {
//		String token = request.get("token");
//		Map<String, Object> response = new HashMap<>();
//
//		if (token != null && jwtTokenProvider.validateToken(token)) {
//			Authentication authentication = jwtTokenProvider.getAuthentication(token);
//			SecurityContextHolder.getContext().setAuthentication(authentication);
//
//			response.put("valid", true);
//			response.put("authentication", authentication);
//		} else {
//			response.put("valid", false);
//		}
//
//		return response;
//	}
	
}
