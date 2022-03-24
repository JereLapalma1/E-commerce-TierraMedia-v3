package controller.promociones;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Atraccion;
import model.PromocionAbsoluta;
import model.TipoAtraccion;
import services.AtraccionService;
import services.PromocionServicio;

@WebServlet("/insertar-promo-absoluta.do")
public class InsertarPromoAbsolutaServlet extends HttpServlet implements Servlet {

	private static final long serialVersionUID = -3439695848950294400L;
	private PromocionServicio promoService;
	private AtraccionService atraccionService;

	@Override
	public void init() throws ServletException {
		super.init();
		this.promoService = new PromocionServicio();
		this.atraccionService = new AtraccionService();
	}

	/*
	 * Muestra el formulario para crear la promo
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/insertar-promo-absoluta.jsp");
		dispatcher.forward(req, resp);
	}

	/*
	 * Envia los datos ingresados por el usuario para crear la promo
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		String nombre = req.getParameter("nombre");
		
		TipoAtraccion tipo = TipoAtraccion.valueOf(req.getParameter("tipoDeAtracciones"));
		
		Double monto = Double.parseDouble(req.getParameter("monto"));
		
		Integer idAtraccion1 = Integer.parseInt(req.getParameter("idAtraccion1"));
		
		Integer idAtraccion2 = Integer.parseInt(req.getParameter("idAtraccion2"));
		
		Atraccion[] pack = new Atraccion[2];
		try {
			pack[0] = atraccionService.buscar(idAtraccion1);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		try {
			pack[1] = atraccionService.buscar(idAtraccion2);
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		
		PromocionAbsoluta promocion = new PromocionAbsoluta();
		try {
			promocion = promoService.crearAbsoluta(nombre, pack, tipo, monto);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		if (promocion.esValida(nombre, pack, tipo, monto)) {
			resp.sendRedirect("/turismoHDR/listar-promo.do");
		} else {
			req.setAttribute("promocion", promocion);

			RequestDispatcher dispatcher = getServletContext()
					.getRequestDispatcher("/insertar-promo-absoluta.jsp");
			dispatcher.forward(req, resp);
		}
	}
}
