jQuery(document).ready(function($) {

	$('.articleThumb').click(function() {
		location.href = "event.main";
	});

});

function follow(user_num, user_name) {
	if (!$('#userIdCache').val())
		return false;
	$.ajax({
		type : 'get',
		url : 'follow.us',
		data : {
			'following' : user_num,
		},
		dataType : 'json',
		success : function(data) {
			if (data == 1) {
				alert('💡' + user_name + '님을 팔로우 하였습니다💡');
			} else if (data <= 0) {
				alert('💡' + user_name + '님을 이미 팔로우 하고있습니다😅');
			}
		} // end success
	}); // end ajax
}

var minihome_check;
function minihome(user_id, user_num, user_name, check) {
	console.log(check);
	console.log(minihome_check);
	if (check == 'mh_popup') {
		if(minihome_check == 1){
			minihome_check = 0;
			return false;
		}else{
			mh_popup(user_id);
		}
	} else if (check == 'follow') {
		follow(user_num, user_name);
		minihome_check = 1;
	}

}