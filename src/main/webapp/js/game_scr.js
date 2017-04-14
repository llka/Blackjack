var inGame = false;
var result = true;
function validateBetForm() {
  var NO_BETS = "Place your bets, please";

  var err =  document.getElementById("betsError");
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
    if(result) {
        $('#dealBtn').blur(function () {
            $.ajax({
                url: '/Ajax',
                data: {
                    command: $('#command').val(),
                    bet1: $('#bet1input').val(),
                    bet2: $('#bet2input').val(),
                    bet3: $('#bet3input').val()
                },
                success: function (responseText) {
                    $('#cards').html(responseText);
                    inGame = true;
                    showDealButton()
                }
            });
        });
    }
});

function showDealButton() {
    var dealBtn =  document.getElementById("dealBtn");
    if(inGame){
        dealBtn.style.visibility = "hidden";
        disableBets();
    }else {
        dealBtn.style.visibility = "visible";
        enableBets();
    }
}

function disableBets() {
    document.getElementById("bet1input").readOnly = true;
    document.getElementById("bet2input").readOnly = true;
    document.getElementById("bet3input").readOnly = true;
}

function enableBets() {
    document.getElementById("bet1input").setAttribute("value","0");
    document.getElementById("bet2input").setAttribute("value","0");
    document.getElementById("bet3input").setAttribute("value","0");
    document.getElementById("bet1input").readOnly = false;
    document.getElementById("bet2input").readOnly = false;
    document.getElementById("bet3input").readOnly = false;
}
