package tp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import tp.servicio.ILibroService;

@Controller
public class LibroController {
	
	private ILibroService _libroService;
	
	@Autowired
	public LibroController(@Qualifier("libroService") ILibroService libroService) {
		_libroService = libroService;
	}
	
	@RequestMapping("eliminar_libro.html")
	public ModelAndView getEliminar(int id) 
	{
		System.out.print(_libroService.readOne(id));
		_libroService.delete(_libroService.readOne(id));
		ModelAndView MV = new ModelAndView();
		MV.setViewName("lista_biblioteca");
		return MV;
	}
}
