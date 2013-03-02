(function ($) {$(function() {
  var converter = new Markdown.Converter();
        
  $('.blog-post').each(function(_, post) {
    $post = $(post);
    
    $body = $post.find('.blog-post-body');
    $body.html(converter.makeHtml($body.html()));
    $body.show();
  });
  
  $('.comment').each(function(_, comment) {
    $comment = $(comment);
    
    $body = $comment.find('.comment-body');
    $body.html(converter.makeHtml($body.html()));
    $body.show();
  });
})})($);