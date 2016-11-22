var Z = {};
Z.isLogging = false;
Z.fn = {
	// 登录
	_btnLogin : function() {
		var params = {
			username : $('#username').val(),
			password : $('#password').val(),
			t : Math.random()
		}
		log('info|login', params);
		$.post('/login', params, function(json) {
			json = $.parseJSON(json);
			if (json.error === 0) {
				log('info|login success')
				Z.fn.showFuncBox(json.data.name);
			} else if (json.error === 1) {
				log('warn|invalid username')
				$('#tipUsername').show('slow').delay(1300).hide('slow');// 显示提示，一段时间后提示消失；hide方法必须有参数，不然延时不起作用
			} else if (json.error === 2) {
				log('warn|wrong password')
				$('#tipPassword').show('slow').delay(1300).hide('slow');// 显示提示，一段时间后提示消失；hide方法必须有参数，不然延时不起作用
			}
		})
	},
	_btnLogout : function(){
		$.post('/logout');
		window.location.href = '/';
	},
	// 隐藏登录框，显示功能区
	showFuncBox : function(name){
		$('#userBox').html(name);
		$('#loginBox').hide();
		$('#funcBox').show();
	},
	_btnJoinRoom : function() {
		$('#joinRoomBox').toggle(300);
	},
	_btnJoinOK : function() {
		var roomId = $('#iptRoom').val();
		log('info|join room', roomId);
		window.location.href = '/meeting.html?' + roomId;
	},
	_btnCreateRoom : function() {
		$.get('/newRoom', {}, function(json) {
			json = $.parseJSON(json);
			if (json.error === 0) {
				log('info|Succeeded to create new room.', 'room number :',
						json.data);
				window.location.href = '/meeting.html?' + json.data;
			} else {
				log('warn|Failed to create new room.', 'massage :', json.message);
			}
		});
	},
	_btnSoloMeeting : function(){
//		$('iptSoloMeeting').val()
	},
	log : function() {
		Z.isLogging ? console.log.apply(this, arguments) : '';
	}
}
var log = Z.fn.log;

$(function() {
	log('Welcome to Video Meeting -- LCRCBANK');
	
	$.get('/currentUser', {} ,function(json){
		json = $.parseJSON(json);
		if(json.error === 0){
			log('info|Yes! Current user is',json.data.name);
			Z.fn.showFuncBox(json.data.name);
		}else{
			log('info|Failed to catch current user',json.message);
		}
	})

	$.each(Z.fn, function(i, ele) {
		if (i.indexOf('_') === 0) {
			$(document).on('click', '#' + i.substr(1), ele);
		}
	})

	log('info|init over');
})