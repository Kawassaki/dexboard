package br.com.dextra.dexboard.servlet;

import br.com.dextra.dexboard.json.IndicadorRespostaJson;
import br.com.dextra.dexboard.service.IndicadorService;
import flexjson.JSONSerializer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class IndicadorRespostasServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");

        Long idProjeto = Long.valueOf(req.getParameter("projeto"));
        Long idIndicador = Long.valueOf(req.getParameter("indicador"));

        List<IndicadorRespostaJson> registrosRespostasDasQuestoes = new IndicadorService().buscarRespostasDasQuestoesDoIndicador(idProjeto, idIndicador);

        JSONSerializer serializer = new JSONSerializer();
        serializer.exclude("*.class", "*.data");
        resp.getWriter().print(serializer.serialize(registrosRespostasDasQuestoes));
    }

}
