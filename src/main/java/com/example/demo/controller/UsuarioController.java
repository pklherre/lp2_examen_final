package com.example.demo.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.UsuarioEntity;
import com.example.demo.service.UsuarioService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioService usuarioService;
	
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

	@GetMapping("/registrar")
	public String showRegistrarUsuario(Model model) {
		
		model.addAttribute("usuario", new UsuarioEntity());
		return "registrar_usuario";	
	}
	
	@PostMapping("/registrar")
	public String registrarUsuario(UsuarioEntity usuarioEntity, Model model, @RequestParam("foto")MultipartFile foto) {
		usuarioService.crearUsuario(usuarioEntity, model, foto);	
		return "registrar_usuario";
	}
	
	@GetMapping("/login")
	public String index(Model model) {
		model.addAttribute("usuario", new UsuarioEntity());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(UsuarioEntity usuarioEntity, Model model, HttpSession session) {
		boolean usuarioValido = usuarioService.validarUsuario(usuarioEntity, session);
		if(usuarioValido) {
			return "redirect:/listado_Productos";
		}
		model.addAttribute("loginInvalido", "No existe el usuario");
		model.addAttribute("usuario", new UsuarioEntity());
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
	    session.invalidate();
	    return "redirect:/login";
	}
}
