var inGame = false;

function validateBetForm() {
  var result = true;
  var NO_BETS = "Place your bets, please";

  var err =  document.getElementById("errorEmptyBets");
  err.style.visibility = "hidden";
  err.innerHTML = "";

  var	bet1 = document.forms["betForm"]["bet1"].value,
      bet3 = document.forms["betForm"]["bet3"].value,
      bet2 = document.forms["betForm"]["bet2"].value;

  if (bet1 < 1 && bet2 < 1 && bet3 < 1) {
      err.style.visibility = "visible";
      err.innerHTML = NO_BETS;
    result = false;
  }
  return result;
}

$(document).ready(function() {
    $('#dealBtn').blur(function() {
        $.ajax({
            url : '/Ajax',
            data : {
                command : $('#command').val(),
                bet1 : $('#bet1input').val(),
                bet2 : $('#bet2input').val(),
                bet3 : $('#bet3input').val()
            },
            success : function(responseText) {
                $('#cards').html(responseText);
                inGame = true;
                showBetForm();
            }
        });
    });
});

function showBetForm() {
    var betForm =  document.getElementById("betForm");
    if(inGame){
        betForm.style.visibility = "hidden";
    }else {
        betForm.style.visibility = "visible";
    }
}
