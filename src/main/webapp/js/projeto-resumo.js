var dexboard = window.dexboard || {};

dexboard.resumo = (function ($, Handlebars) {

    Handlebars.registerHelper("lowercase", function (str) {
        return str.toLowerCase();
    });

    var model = {};
    var service = {};
    var template = null;
    var view = {};

    var isTvMode = function () {
        var search = document.location.search;
        return search.indexOf("from") > 0 && search.indexOf("TV") > 0;
    };

    var queryEquipe = function () {
        var query = document.location.search.substr(1).split("=");
        return (query.length > 1 && query[0] === "equipe") ? query[1] : undefined;
    };

    var queryProjeto = function () {
        var query = document.location.search.substr(1).split("=");
        return (query.length > 1 && query[0] === "projeto") ? query[1] : undefined;
    };
    var semanasEmPerigo = function (indicador) {
        if (indicador.registros.length === 0) {
            return -1;
        }

        var ultimoRegistroEmPerigo;
        for (var i = 0; i < indicador.registros.length; i++) {
            var registro = indicador.registros[i];

            if (registro.classificacao !== 'PERIGO') {
                break;
            }

            ultimoRegistroEmPerigo = registro;
        }

        if (!ultimoRegistroEmPerigo) {
            return -1;
        }

        return Math.floor((new Date().getTime() - (new Date(ultimoRegistroEmPerigo.data).getTime())) / 1000 / 60 / 60 / 24 / 7);
    };

    var labelSemanasEmPerigo = function (semanas) {
        return semanas < 0 ? '' : semanas + '';
    };

    model.StatusHistogram = function () {

        var self = this;

        this.quantidadeAtraso = 0;
        this.quantidadePerigo = 0;
        this.quantidadeAtencao = 0;
        this.quantidadeOk = 0;

        this.addQuantidade = function (status) {
            if (status === "ATRASADO") {
                self.quantidadeAtraso = self.quantidadeAtraso + 1;
            } else if (status === "PERIGO") {
                self.quantidadePerigo++;
            } else if (status === "ATENCAO") {
                self.quantidadeAtencao++;
            } else {
                self.quantidadeOk++;
            }
        };

    };

    model.Indicador = function (jsonIndicador) {

        var self = this;

        this.id = jsonIndicador.id;
        this.nome = jsonIndicador.nome;
        this.status = new model.StatusHistogram();
    };

    model.Indicador.fromProjetos = function (projetos) {
        if (!projetos[0]) return [];

        var map = {};
        var indicadores = projetos[0].indicadores.map(function (jsonIndicador) {
            var indicador = new model.Indicador(jsonIndicador);
            map[jsonIndicador.id] = indicador;
            return indicador;
        });

        projetos.forEach(function (projeto) {
            projeto.indicadores.forEach(function (json) {
                var status = json.atrasado ? "ATRASADO" : json.classificacao;
                map[json.id].status.addQuantidade(status);
            });
        });

        return indicadores;
    };

    model.QueryWrapper = function (projetos) {

        var self = this;

        this.projetos = projetos || [];
        this.indicadores = model.Indicador.fromProjetos(this.projetos);
        this.tvMode = isTvMode();

        this.status = new model.StatusHistogram();

        projetos.forEach(function (projeto) {
            projeto.semanasEmPerigoSatisfacaoEquipe = labelSemanasEmPerigo(semanasEmPerigo(projeto.indicadores[4]));

            var status = projeto.atrasado ? "ATRASADO" : projeto.classificacao;
            self.status.addQuantidade(status);
        });

    };

    service.query = function () {
        $.getJSON("/query").done(function (projetos) {
            var queryWrapper = new model.QueryWrapper(projetos);
            view.render(queryWrapper);
        });
    };

    view.init = function () {
        service.query();
    };

	view.render = function(projetos) {
		var projetoQuery = decodeURIComponent(queryProjeto());
		var projeto = projetos.projetos.find(item => {
			return item.nome.toLowerCase() == projetoQuery.toLowerCase();
		})

		projeto.indicadores.forEach((indicador) => {
			indicador.registros.forEach((registro) => {
				registro.comentarioFormatado = [];
				let registroSeparado = registro.comentario.split("\n");
				registroSeparado.forEach((item) => {
					registro.comentarioFormatado.push(item);
				});
			});
		});

		console.log(projeto);

		var source = $("#slides-apresentacao-resumo").html();
		var template = Handlebars.compile(source);
        $("#resumo-overlay").addClass("presentation-resumo");
		$("#resumo-overlay").html(template({
			"projeto" : projeto
		}));
	}
    return {
        "model": model,
        "view": view,
        "service": service
    };

})(jQuery, Handlebars);
