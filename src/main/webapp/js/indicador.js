var dexboard = window.dexboard || {};

dexboard.indicador = (function($, Handlebars) {
	
	var dialog = {};
	var service = {};
	var template = null;
	var view = {};
	
	var stopPropagation = function(event) {
		if (event) {
			event.preventDefault();
			event.stopPropagation(); 
		}
		return false;
	};
	
	dialog.Buttons = function(indicador) {
		
		var self = this;
		var hasRecords = indicador.registros ? indicador.registros.length > 0 : false;
		
		this.change = $("#dialog input.change");
		this.keep = $("#dialog input.btn-sem-mudancas");
		
		var enableChange = function() {
			self.change.prop("disabled", false);
			self.keep.prop("disabled", true);
		};
		
		var disableChange = function() {
			self.change.prop("disabled", true);
			if (hasRecords) {
				self.keep.prop("disabled", false);
			}
		};
		
		this.toggle = function(on) {
			if (on) {
				enableChange();
			} else {
				disableChange();
			}
		};
		
		this.init = function(projeto) {
			
			var textArea = new dialog.TextArea();
			var popup = new view.Dialog();
			
			self.change.click(function(e) {
                var respostas = [];

                if(indicador.questoes && indicador.questoes.length > 0){
                    $.each($('#questoes-form').serializeArray(), function(i, field) {
                        respostas.push({
                            composeId: field.name,
                            conteudo: field.value
                        });
                    });
                    if(respostas.length > 0 && respostas.length != indicador.questoes.length){
                        alert('Responda todas as questões para continuar.');
                        return;
                    }
                }

				var button = $(this);
				
				if (!button.prop("disabled")) {
					var comentario = textArea.val();
					var status = button.prop("name");
					service.novoStatus(projeto, indicador, status, comentario, respostas);
					popup.close();
				}
			});
			
			self.keep.click(function(e) {
				var button = $(this);
				
				if (!button.prop("disabled")) {
					var ultimo = indicador.registros[0];
					var comentario = ultimo.comentario;
					var status = ultimo.classificacao;
					service.novoStatus(projeto, indicador, status, comentario);
					popup.close();
				}
			});
			
			return self;
		};
	};
	
	dialog.Records = function() {
		
		var self = this;
		
		this.records = $("#dialog ul.historico");
		this.title = $(".toggle-historico");
		this.toggleChar = this.title.find("span");
		
		var isVisible = function() {
			return self.records.is(":visible");
		};
		
		var toggle = function(on) {
			if (on) {
				self.records.show();
				self.toggleChar.html("-");
			} else {
				self.records.hide();
				self.toggleChar.html("+");
			}
		};
		
		this.init = function() {
			self.title.mousedown(stopPropagation);
			
			self.title.click(function(e) {
				stopPropagation(e);
				toggle(!isVisible());
			});
			
			return self;
		};
	};
	
	dialog.TextArea = function() {
		
		var self = this;
		
		this.el = $("#dialog textarea");
		
		this.isEmpty = function() {
			return self.el.val().length === 0; 
		};
		
		this.val = function() {
			return self.el.val();
		};
		
		this.init = function(buttons) {
			self.el.keyup(function() {
				buttons.toggle(!self.isEmpty());
			});
			
			return self;
		};
		
	};
	
	service.novoStatus = function(projeto, indicador, status, comentario, respostas = []) {
		return $.ajax({
				"type" : "POST",
				"url" : "/indicador",
				"data" : {
					"projeto" : projeto.idPma,
					"indicador" : indicador.id,
					"respostas": JSON.stringify(respostas),
					"registro" : JSON.stringify({
						"classificacao" : status,
						"comentario" : comentario
					})
				}
		}).then(function(registro) {
			indicador.atrasado = false;
			indicador.classificacao = registro.classificacao;
			indicador.registros.unshift(registro);
			
			projeto.atrasado = projeto.indicadores.filter(e => e.ativo).some(function(i) {
				return i.atrasado;
			});
			
			projeto.classificacao = projeto.indicadores.filter(e => e.ativo).reduce(function(classificacao, i) {
				if (classificacao === "PERIGO" || i.classificacao === "PERIGO") {
					return "PERIGO";
					
				} else if (classificacao === "ATENCAO") {
					return "ATENCAO";
					
				} else {
					return i.classificacao;
				}
			});
			
		}).done(function() {
			var container = (new dexboard.projeto.view.Projeto()).container[0];
			var event = new CustomEvent("update-indicador", {"detail" : projeto});
			container.dispatchEvent(event);
		});
	};
	
	view.Dialog = function() {
		
		var self = this;
		
		this.container = $("#dialog");
		this.overlay = $("#dialog-overlay");
		
		this.close = function(event) {
			stopPropagation(event);
			self.overlay.hide();
			self.container.dialog("close");
		};
		
		this.open = function(projeto, indicador) {

			var t = new dexboard.projeto.view.Projeto();
			var column = t.container.find("tr.chosen");
			if (column.length !== 0) {
			    return;
			}

		    indicador.questionsByCategory = orderQuestionsByCategory(indicador.questoes);

			self.container.html(template({
				"projeto" : projeto,
				"indicador" : indicador
			}));
			
			self.overlay.show();
			self.container.dialog("open");
			
			self.container.find(".dialog-close-button").click(self.close);
			
			var buttons = (new dialog.Buttons(indicador)).init(projeto);
			(new dialog.TextArea()).init(buttons);
			(new dialog.Records()).init();
			
			return dialog;
		};
		
		this.init = function() {
			self.overlay.click(self.close);
			
			$(document).keydown(function(e) {
				if (e.which === 27) {
					self.close(e);
				}
			});
			
			self.container.dialog({
				"autoOpen" : false,
				"dialogClass" : "no-close dialog-overflow",
				"width" : 850,
				open: function (){
				    $(".ui-dialog").scrollTop(0);
				}
			});
		};

	};
	
	view.init = function() {
		var source = $("#edicao-indicador-template").html();
		template = Handlebars.compile(source);
		(new view.Dialog()).init();
	};

	return {
		"view" : view,
		"service" : service
	};
	
})(jQuery, Handlebars);
