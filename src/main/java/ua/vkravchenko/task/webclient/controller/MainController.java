package ua.vkravchenko.task.webclient.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.type.MapType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ua.vkravchenko.task.webclient.entity.User;
import ua.vkravchenko.task.webclient.jms.MessageSender;

@Controller
public class MainController {
	
	@Autowired
	private MessageSender messageSender;

	@RequestMapping({"/index", "/"})
	public String mainPage(Model model) {
		return "index";
	}
	
	// Обрабатывает запрос от пользователя на добавление/удаление/получение информации из базы данных
	@RequestMapping(value = "/index", method = RequestMethod.POST)
	public String makeAction(Model model, @RequestParam(value = "commandType", required=true) String commandType, @RequestParam(value = "userId", required=false) String userId, 
			@RequestParam(value = "userName", required=false) String userName, @RequestParam(value = "surname", required=false) String surname) {
		
		// Комманды, которые могут обрабатыватся приложением
		List<String> availableCommands = new ArrayList<String>();
		availableCommands.add("none");
		availableCommands.add("add");
		availableCommands.add("get");
		availableCommands.add("remove");
		
		// Для того, чтобы знать, что конкретно отображать на jsp странице
		model.addAttribute("previousCommandType", commandType);
		
		if ("none".equals(commandType)) {
			model.addAttribute("status", "WARN");
			model.addAttribute("message", "You must specify command");
			return "index";
		}
		
		if (!availableCommands.contains(commandType)) {
			model.addAttribute("message", "You must specify command");
			return "index";
		}
		
		if (commandType.equals("add") && (userName.equals("") || surname.equals(""))) {
			model.addAttribute("status", "WARN");
			model.addAttribute("message", "You must specify user's name and surname");
			return "index";
		}
		
		User user = new User();
		if (userId == null || userId.equals("null") || userId.equals("")) {
			user.setId(-1);
		} else {
			user.setId(Integer.parseInt(userId));
		}
		
		if (userName == null || userName.equals("null") || userName.equals("")) {
			user.setName("");
		} else {
			user.setName(userName);
		}
		
		if (surname == null || surname.equals("null") || surname.equals("")) {
			user.setSurname("");
		} else {
			user.setSurname(surname);
		}
		
		try {
			
			// Отошлем серверу HashMap, которая будет содержать комманду и информацию о пользователе
			Map<String, String> map = new HashMap<String, String>();
			ObjectWriter objectWriter = new ObjectMapper().writer();
			String jsonUser = objectWriter.writeValueAsString(user);
			map.put("command", commandType);
			map.put("value", jsonUser);
			
			String json = objectWriter.writeValueAsString(map);
			
			// Отсылаем сообщение и ждем ответ
			String response = messageSender.sendMessage(json);
			if (response == null) {
				model.addAttribute("status", "FAIL");
				model.addAttribute("message", "Some error with JMS service occured");
				return "index";
			}
			
			// В ответ получаем HashMap со статусом обработки запроса и нужной информацией (сообщением либо массивом пользователей)
			ObjectMapper objectMapper = new ObjectMapper();
			MapType mapType = objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, String.class);
			Map<String, String> responseMap = objectMapper.readValue(response, mapType);
			String status = responseMap.get("status");
			String message = responseMap.get("message");
			if (status.equals("OK")) {
				if ("get".equals(commandType)) {
					List<User> users = objectMapper.readValue(message, objectMapper.getTypeFactory().constructCollectionType(
		                    List.class, User.class));
					model.addAttribute("users", users);
				} else {
					model.addAttribute("message", message);
				}
				
			} else {
				model.addAttribute("message", message);
			}
			model.addAttribute("status", status);
			
		} catch (JsonGenerationException e) {
			model.addAttribute("status", "FAIL");
			model.addAttribute("message", "Cannot generate json");
			return "index";
		} catch (JsonMappingException e) {
			model.addAttribute("status", "FAIL");
			model.addAttribute("message", "Cannot map json");
			return "index";
		} catch (JMSException e) {
			model.addAttribute("status", "FAIL");
			model.addAttribute("message", "JMS Exception occured");
			return "index";
		} catch (IOException e) {
			model.addAttribute("status", "FAIL");
			model.addAttribute("message", "IO Exception occured");
			return "index";
		}
		
		return "index";
	}
	
}
