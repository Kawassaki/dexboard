package br.com.dextra.dexboard.dao;

import br.com.dextra.dexboard.domain.Indicador;
import br.com.dextra.dexboard.domain.Notificacao;
import br.com.dextra.dexboard.domain.Projeto;
import br.com.dextra.dexboard.domain.RegistroAlteracao;
import br.com.dextra.dexboard.utils.Config;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

public class NotificacaoDao {

	private static final Logger LOG = LoggerFactory.getLogger(NotificacaoDao.class);

	private static final int DIAS_PARA_RENOTIFICAR = 3;

	private Objectify ofy;

	public NotificacaoDao() {
		RegisterClasses.register();
		ofy = ObjectifyService.ofy();
	}

	public static int getValidadeAlteracao() {
		String validadeProp = System.getProperty("validade");
		if (validadeProp != null) {
			return Integer.parseInt(validadeProp);
		}
		return 30;
	}

	public List<Projeto> buscarProjetosParaNotificar() {
		List<Projeto> projetosAtrasados = buscarProjetosAtrasados();
		List<Projeto> projetosParaNotificar = new ArrayList<>();

		for (Projeto projeto : projetosAtrasados) {

			Notificacao notificacao = buscarUltimaNotificacao(projeto);

			if (notificacao == null || calculaDiasAtras(notificacao.getDate()) >= DIAS_PARA_RENOTIFICAR) {
				projetosParaNotificar.add(projeto);
			}
		}

		return projetosParaNotificar;
	}

	private Notificacao buscarUltimaNotificacao(Projeto projeto) {
		return ofy.load().type(Notificacao.class).id(projeto.getIdPma()).now();
	}

	private List<Projeto> buscarProjetosAtrasados() {
		List<Projeto> projetosAtrasados = new ArrayList<>();
		int validadeAlteracao = getValidadeAlteracao();

		List<Projeto> projetos = buscarProjetos();
		for (Projeto projeto : projetos) {
			List<Indicador> indicadores = buscarIndicadores(projeto);
			for (Indicador indicador : indicadores) {
				if (indicador.getAtivo() == true) {
					RegistroAlteracao registroAlteracao = buscarRegistrosAlteracoes(projeto, indicador);

					if (registroAlteracao == null) {
						projetosAtrasados.add(projeto);
						break;
					}

					Date dataAlteracao = registroAlteracao.getData();
					long diasAtras = calculaDiasAtras(dataAlteracao);

					if (diasAtras >= validadeAlteracao) {
						projetosAtrasados.add(projeto);
						break;
					}
				}
			}

		}
		return projetosAtrasados;
	}

	private long calculaDiasAtras(Date data) {
		return (new Date().getTime() - data.getTime()) / (1000 * 60 * 60 * 24);
	}

	private RegistroAlteracao buscarRegistrosAlteracoes(Projeto projeto, Indicador indicador) {
		return ofy.load().type(RegistroAlteracao.class).filter("indicador", indicador).order("-data").first().now();
	}

	private List<Indicador> buscarIndicadores(Projeto projeto) {
		return ofy.load().type(Indicador.class).filter("projeto", projeto).list();
	}

	private List<Projeto> buscarProjetos() {
		return ofy.load().type(Projeto.class).filter("ativo", true).list();
	}

	public void notificarEquipeProjeto(Projeto projeto) {
		enviarEmailEquipeProjeto(projeto);
		registrarDataNotificacao(projeto);
	}

	private void enviarEmailEquipeProjeto(Projeto projeto) {
		Properties props = new Properties();
		Session session = Session.getDefaultInstance(props, null);

		Message msg = new MimeMessage(session);

		LOG.info("Enviando email de notificacao: projeto=" + projeto.getNome() + ", equipe=" + projeto.getEquipe() + ", email="
				+ projeto.getEmail());

		if ((projeto.getEmail() == null || projeto.getEmail().isEmpty()) || (projeto.getEquipe() == null || projeto.getEquipe().isEmpty())) {
			LOG.info("Notificacão cancelada pois não foi encontrado(a) o(a) e-mail/equipe.");
			return;
		}

		try {
			String from = Config.getProperty("dxb.sender", "dexboard@dexboard-reload-homolog.appspotmail.com");
			LOG.info(String.format("Enviando email como %s", from));
			msg.setFrom(new InternetAddress(from,"Dexboard Reload"));
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(projeto.getEmail(), projeto.getEquipe()));
			addBccs(msg);
			msg.setSubject("[dexboard] Atualizar projeto: " + projeto.getNome());
			msg.setContent(createMessageBody(projeto), "text/html");
			Transport.send(msg);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
	}

	private void addBccs(Message msg) throws MessagingException, UnsupportedEncodingException {
		String bccProperty = Config.getProperty("dxb.bcc", "fernando@dextra-sw.com");
		String[] bccs = bccProperty.replaceAll("\\s+", "").split(",");
		for (String bcc : bccs) {
			msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc, bcc));
		}
	}

	private String createMessageBody(Projeto projeto) {
		return String.format("<p><b>%s</b>, n&atilde;o deixe seu projeto <b>%s</b> ficar desatualizado...</p>",
				projeto.getEquipe(), projeto.getNome()) +
				"<p>Acesse o dexboard para atualiz&aacute;-lo: <a href=\"http://dexboard-reload.appspot.com\">dexboard-reload.appspot.com</a></p>" +
				"<img width=300 src=\"" + getImg() + "\"/>";
	}

	private String getImg() {
		String[] imgs = {"https://bensbitterblog.files.wordpress.com/2013/05/brst-thing-on-a-lazy-saturday-77986722415.jpg?w=676",
				"http://itsinfoworld.com/wp-content/uploads/2015/03/Being-lazy.jpg",
				"http://www.hercampus.com/sites/default/files/2014/04/18/Our_lazy_cat_by_tariqphoto.jpg",
				"https://academichelp.net/wp-content/uploads/2014/01/laziness.jpg",
				"http://ecdn.funzypics.com/grumpycatmemes/pics/25/Laziness-Is-The-Mother-Of-Improvisation.jpg",
				"https://farm8.staticflickr.com/7639/16830167905_fafc311f5a_o.jpg"};
		return imgs[randomIndex(imgs)];
	}

	private int randomIndex(String[] imgs) {
		return new Random().nextInt(imgs.length);
	}

	private void registrarDataNotificacao(Projeto projeto) {
		Notificacao notificacao = new Notificacao();
		notificacao.setDate(new Date());
		notificacao.setIdPma(projeto.getIdPma());
		ofy.save().entity(notificacao).now();
	}
}
