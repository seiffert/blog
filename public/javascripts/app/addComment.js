(function($){$(function(){
  var editor = new EpicEditor({
    basePath: '/assets/epiceditor',
    file: {
      name: 'new comment',
      defaultContent: '',
      autoSave: 100
    },
  }).load();
    
  $('#addCommentSubmit').on('click', function() {
    var body = editor.exportFile();
    
    $('#commentBody').val(body);
  });
})})(jQuery);