$(function() {

  var $terminal = $('#terminal');
  var $output = $('#output');
  var $promptElem = $('#prompt');
  var $form = $('form');
  var $input = $form.find('input');
  var promptText = $terminal.data('prompt');

  var commands = {
    'clear': function() { $output.html(''); }
  };

  $promptElem.text(promptText);
  $input.width($form.width() - $promptElem.width() - 30);

  function output(command, response) {
    var klass = response.statusText == 'OK' ? 'success' : 'failure';
    $output.append($('<div>').addClass('command ' + klass).html('<strong>' + response.status + '&gt;</strong> ' + command));
    $output.append($('<pre>').text(response.responseText));
    $('body')[0].scrollTop = 99999;
  }

  $input.focus();
  $form.on('submit', function() {
    $form.addClass('processing');
    var command = $input.val();
    $input.val('');
    if (typeof commands[command] != 'undefined') {
      commands[command]();
      $form.removeClass('processing');
      $input.focus();
    }
    else {
      $.ajax({
        url: $form.attr('action'),
        type: 'post',
        data: { command: command },
        success: function(a, b, response) { output(command, response); },
        error: function(response) { output(command, response, true); },
        complete: function(response) {
          $form.removeClass('processing');
          $input.focus();
        }
      });
    }
  return false;
  });
});
