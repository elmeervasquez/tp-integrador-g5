package tp.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import tp.Request.CreateClienteRequest;
import tp.dominio.Cliente;
import tp.dominio.Localidad;
import tp.dominio.Nacionalidad;
import tp.dominio.Persona;
import tp.servicio.IClienteService;
import tp.servicio.ILocalidadService;
import tp.servicio.INacionalidadService;
import tp.servicioImpl.ClienteService;
import common.Dictionary;
import common.Directory;
import common.Error;
import common.Status;

@Controller
@SessionAttributes({"newCliente"})
public class ClienteController {
	
	private IClienteService _clienteService;
	private INacionalidadService _nacionalidadService;
	private ILocalidadService _localidadService;
	
	@Autowired
	public ClienteController(
			@Qualifier("clienteService") IClienteService clienteService, 
			@Qualifier("nacionalidadService") INacionalidadService nacionalidadService, 
			@Qualifier("localidadService") ILocalidadService localidadService
			)
	{
		_clienteService = clienteService;
		_nacionalidadService = nacionalidadService;
		_localidadService = localidadService;
	}
	
	@RequestMapping("lista_clientes.html")
	public ModelAndView getMenuPrincipal() {
		ModelAndView MV = new ModelAndView();
		try {
			List<Cliente> lista = _clienteService.selectList();
			MV.addObject("clientesList", lista);
			MV.setViewName(getPath("cliente"));
		} catch (Exception ex) {
			MV.addObject("error", Error.INTERNAL_CONTROLLER_ERROR);
		}
		return MV;
	}
	
	@RequestMapping(value="create_cliente.html", method=RequestMethod.GET)
	public ModelAndView getClienteCreate(@ModelAttribute("persona") Persona persona) {
		ModelAndView MV = new ModelAndView();
		try {
			MV.addObject("nacionalidadList", _nacionalidadService.selectList());
			MV.addObject("localidadList", _localidadService.selectList());
			MV.setViewName(getPath("cliente-create"));
		} catch (Exception ex) {
			MV.addObject("error", Error.INTERNAL_CONTROLLER_ERROR);
		}
		return MV;
	}
	
	@RequestMapping(value="create_cliente.html", method=RequestMethod.POST)
	public ModelAndView postClienteCreate(CreateClienteRequest request) {
		ModelAndView MV = new ModelAndView();
		try {
			
			Nacionalidad nac = _nacionalidadService.readOne(request.getNacionalidadId());
			Localidad loc = _localidadService.readOne(request.getLocalidadId());
			

			MV.addObject("status", Status.getGenerateStatus(_clienteService.create(new Cliente(request, nac, loc))));
			MV.addObject("clientesList", _clienteService.selectList());
			MV.setViewName(getPath("cliente"));
		} catch (Exception ex) {
			MV.addObject("error", Error.INTERNAL_CONTROLLER_ERROR);
		}
		return MV;
	}
	
	@ModelAttribute("newCliente")
	public Persona postEmptyCliente() {
		Persona emptyCliente = new Persona();
		return emptyCliente;
	}
	
	@RequestMapping("delete_cliente.html")
	public ModelAndView deleteCliente(String clienteId) {
		ModelAndView MV = new ModelAndView();
		try {
			MV.addObject("status", Status.getDeleteStatus(_clienteService.delete(Integer.parseInt(clienteId))));
			MV.addObject("clientesList", _clienteService.selectList());
			MV.setViewName(getPath("cliente"));
		} catch (Exception ex) {
			MV.addObject("error", Error.INTERNAL_CONTROLLER_ERROR);
		}
		return MV;
	}
	
	@RequestMapping("update_cliente.html")
	public ModelAndView updateCliente(@ModelAttribute Cliente cliente) {
		ModelAndView MV = new ModelAndView();
		try {
			MV.addObject("status", Status.getUpdateStatus(_clienteService.update(cliente)));
			MV.setViewName("cliente");
		} catch (Exception ex) {
			MV.addObject("error", Error.INTERNAL_CONTROLLER_ERROR);
		}
		return MV;
	}
	
	@RequestMapping("search_cliente.html")
	public ModelAndView getClienteByProperty(String inputText, String propertySelect) {
		ModelAndView MV = new ModelAndView();
		try {
			List<Cliente> lista = new ArrayList<Cliente>();
			if (propertySelect.equals("default") || inputText.isEmpty() || ((propertySelect.equals("persona.dni") || propertySelect.equals("persona.telefono")) && !Dictionary.isNumeric(inputText))) {
				lista = _clienteService.selectList();
			} else {
				lista = _clienteService.selectListByProperty(propertySelect, inputText);
				MV.addObject("inputValue", inputText);
			}
			MV.addObject("clientesList", lista);
			MV.setViewName(getPath("cliente"));
		} catch (Exception ex) {
			MV.addObject("error", Error.INTERNAL_CONTROLLER_ERROR);
		}
		return MV;
	}
	
	private String getPath(String jsp) {
		return Directory.CLIENTE + jsp;
	}

}
