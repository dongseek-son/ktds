package com.ktds.member.web;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ktds.common.session.Session;
import com.ktds.member.service.MemberService;
import com.ktds.member.validator.MemberValidator;
import com.ktds.member.vo.MemberVO;
import com.ktds.security.User;

@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@GetMapping("/member/logout")
	public String doMemberLogoutAction( HttpSession session ) {
		session.invalidate();
		return "redirect:/member/login";
	}
	
	@GetMapping("member/regist")
	public String viewMemberJoinPage() {
		return "member/regist";
	}
	
	@PostMapping("/member/check/duplicate")
	@ResponseBody
	public Map<String, Object> doCheckDuplicateEmail( @RequestParam String email ) {
		Map<String, Object> result = new HashMap<>();
		result.put("status", "OK");
		result.put("duplicated", new Random().nextBoolean());
		return result;
	}
	
	@PostMapping("/member/check/password")
	@ResponseBody
	public Map<String, Object> doCheckPasswordPattern( @RequestParam String password ) {
		Map<String, Object> result = new HashMap<>();
		
		String passwordPolicy = "((?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{8,})";
		
		Pattern pattern = Pattern.compile(passwordPolicy);
		Matcher matcher = pattern.matcher(password);
		
		result.put("status", "OK");
		result.put("available", matcher.matches() );
		
		return result;
	}
	
	@PostMapping("member/regist")
	public ModelAndView doMemberJoinAction( 
			@Validated({MemberValidator.Regist.class}) @ModelAttribute MemberVO memberVO
			, Errors errors ) {
		if ( errors.hasErrors() ) {
			ModelAndView view = new ModelAndView("member/regist");
			view.addObject("memberVO", memberVO);
			return view;
		}
		else {
			this.memberService.createMember(memberVO);
			return new ModelAndView("member/login");
		}	
	}
	
	@GetMapping("member/login")
	public String viewMemberLoginPage() {
		return "member/login";
	}
	
	@RequestMapping("member/login.do")
	public ModelAndView doMemberLoginAction( 
			@ModelAttribute MemberVO memberVO
			, Errors errors
			, HttpSession session ) {
		
		ModelAndView view = new ModelAndView("member/login");
		
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getDetails();
		memberVO.setEmail(user.getUsername());
		memberVO.setPassword(user.getPassword());
		
		if ( errors.hasErrors() ) {
			view.addObject("message", "로그인에 오류가 생겼습니다.");
			return view;
		}
		
		if ( user.isAccountNonLocked() ) {
			view.addObject("message", "해당 계정은 3회이상 비밀번호가 틀렸습니다. 1시간 이후에 다시 시도해주세요.");
			return view;
		}
		
		MemberVO loginMemberVO = this.memberService.loginMember(memberVO);
			
		if( loginMemberVO != null ) {
			session.setAttribute(Session.USER, loginMemberVO);
			session.setAttribute(Session.TOKEN, UUID.randomUUID().toString());
			view.setViewName("redirect:/board/list");
			return view;
		}
		else {
			view.addObject("message", "이메일과 아이디가 올바르지 않습니다.");
		}
		
		return view;
	}
	
}
