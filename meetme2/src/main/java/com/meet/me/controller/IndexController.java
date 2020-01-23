package com.meet.me.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.meet.me.domain.Event;
import com.meet.me.domain.User;
import com.meet.me.service.EventService;
import com.meet.me.service.UserService;

@Controller
public class IndexController {
	@Autowired
	private EventService eventService;
	
	@Autowired
	private UserService userService;
	
	//메인 화면 -카테고리, 해시태그 값을 가져감
	@GetMapping(value="/main.index")
	public ModelAndView index(ModelAndView mv, HttpServletResponse response,HttpServletRequest request, HttpSession session, 
								@RequestParam(value = "note", defaultValue = "false") String note) {
		response.setContentType("text/html;charset=UTF-8");
		
		String id = "";
		
		if(session.getAttribute("user_id1") != null)
			id = session.getAttribute("user_id1").toString();
		
		int user_num = 0;
		
		if(session.getAttribute("user_id1") != null)
			user_num = Integer.parseInt(session.getAttribute("user_num1").toString());
		System.out.println(user_num);
		List<Event> event = eventService.getIntrestingEvent(id);
		List<User> RandomUser = userService.getRandomUser(user_num);
		List<String> category = eventService.getCategory();

		System.out.println(RandomUser.size());
		for(User u : RandomUser)
		System.out.println(u.getUser_id());
		
		for(Event e : event) {
			String title = e.getEVENT_TITLE();
			String context = e.getEVENT_CONTENT();
			if(context.length() > 250)
				e.setEVENT_CONTENT(context.substring(0, 250)+" ...");
			if(title.length() > 25)
				e.setEVENT_TITLE(title.substring(0, 25)+" ...");
		}
		
		mv.setViewName("main/main");
		mv.addObject("event", event);
		mv.addObject("category", category);
		mv.addObject("RandomUser", RandomUser);
		mv.addObject("note", note);
		
		mv.addObject("HeaderComment", "The real world is calling");
		
		
		return mv;
	}
}
