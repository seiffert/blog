(function($){$(function(){
  var editor = new EpicEditor({
    basePath: 'assets/epiceditor',
    file: {
      name: 'new post',
      defaultContent: '',
      autoSave: 100
    },
  }).load();
    
  $('#addBlogPostSubmit').on('click', function() {
    var body = editor.exportFile();
    
    $('#blogPostBody').val(body);
  });
})})(jQuery);