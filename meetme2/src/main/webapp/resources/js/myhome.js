function mh_popup(user_id){
	var popupX = (window.screen.width / 2) - (1200 / 2);
	// 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음
	var popupY= (window.screen.height / 2) - (700 / 2);
	// 만들 팝업창 상하 크기의 1/2 만큼 보정값으로 빼주었음
	window.open("mmain.mh?id="+user_id,'','width=1200, height=700, resizable=no, left='+ popupX + ', top='+ popupY, 'scrollbars=yes');
}

function event_popup(event_num){
	var popupX = (window.screen.width / 2) - (1200 / 2);
	// 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음
	var popupY= (window.screen.height / 2) - (700 / 2);
	// 만들 팝업창 상하 크기의 1/2 만큼 보정값으로 빼주었음
	window.open("event.main?event="+event_num,'','width=1200, height=700, resizable=no, left='+ popupX + ', top='+ popupY, 'scrollbars=yes');
}

function board_popup(user_id){
	var popupX = (window.screen.width / 2) - (1200 / 2);
	// 만들 팝업창 좌우 크기의 1/2 만큼 보정값으로 빼주었음
	var popupY= (window.screen.height / 2) - (700 / 2);
	// 만들 팝업창 상하 크기의 1/2 만큼 보정값으로 빼주었음
	window.open("mboard.mh?id="+user_id,'','width=1200, height=700, resizable=no, left='+ popupX + ', top='+ popupY, 'scrollbars=yes');
}