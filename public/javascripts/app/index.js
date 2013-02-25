(function ($) {$(function() {
  var converter = new Markdown.Converter();
        
  $('.blog-post').each(function(_, post) {
    $post = $(post);
    
    $body = $post.find('.blog-post-body');
    $body.html(converter.makeHtml($body.html()));
    $body.show();
  });
})})($);