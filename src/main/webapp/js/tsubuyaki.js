function newUser() {
    $('#operation_input').val('new');
    $('#user_form').submit();
}

function editUser(id) {
    $('#operation_input').val('edit');
    $('#user_id_input').val(id);
    $('#user_form').submit();
}

function deleteUser(id) {
    const ret = confirm('ユーザーを削除してもよろしいですか?');
    if(ret) {
        $('#operation_input').val('delete');
        $('#user_id_input').val(id);
        $('#user_form').submit();
    }
}
