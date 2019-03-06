package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.service.PlanilhaService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExportServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        new PlanilhaService().exportarPlanilha();
        resp.getWriter().print("Exportar Planilha");
    }

}
