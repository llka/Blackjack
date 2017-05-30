var inGame = false;
var resultA = true;
var hands = 0;

function validateBetForm() {
  var NO_BETS = "Place your bets, please";
  var NOT_ENOUGH_MONEY = "Bets are bigger than your balance, try bet less";

  var err =  document.getElementById("betsError");
  err.style.visibility = "hidden";
  err.innerHTML = "";

    var	bet1 = +document.forms["betForm"]["bet1"].value,
        bet3 = +document.forms["betForm"]["bet3"].value,
        bet2 = +document.forms["betForm"]["bet2"].value;
    var balance = +document.forms["betForm"]["balance"].value;

    if(bet1>0){
        hands++;
    }
    if(bet2>0){
        hands++;
    }
    if(bet3>0){
        hands++;
    }
  if (bet1 < 1 && bet2 < 1 && bet3 < 1) {
      err.style.visibility = "visible";
      err.innerHTML = NO_BETS;
      resultA = false;
  }else if (bet1+bet2+bet3 > balance){
      err.style.visibility = "visible";
      err.innerHTML = NOT_ENOUGH_MONEY;
      resultA = false;
  }
  else {
      $(document).ready(function () {
          $('#languageEn').attr("href","#");
          $('#languageRu').attr("href","#");
          $('.footer').attr("title","Unavailable to change language while playing");
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
                  showDealButton();
                  checkForInsurance();
              }
          });
      });
  }
}

function checkForInsurance() {
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "CheckForInsurance"
            },
            success: function (responseText) {
                $('#gameButtons').html(responseText);
            }
        });
    });
}

function showDealButton() {
    var dealBtn =  document.getElementById("dealBtn");
    if(inGame){
        dealBtn.style.display = "none";
        disableBets();
    }else {
        dealBtn.style.display = "block";
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

var insureTimes = 0;
function insure(handId) {
    insureTimes++;
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "InsureBet",
                betPlace: handId
            },
            success: function () {
                var insureButton =  document.getElementById("insure" + handId);
                insureButton.style.visibility = "hidden";
                var insureButtonNot =  document.getElementById("insureNot" + handId);
                insureButtonNot.style.visibility = "hidden";

                if(insureTimes == hands){
                    var insureButtonsRow =  document.getElementById("insuranceButtons");
                    insureButtonsRow.style.display = "none";
                    insureTimes = 0;
                    showActionButtons();
                }
            }
        });
    });
}

function insureNot(handId) {
    insureTimes++;
    var insureButton =  document.getElementById("insure" + handId);
    insureButton.style.visibility = "hidden";
    var insureButtonNot =  document.getElementById("insureNot" + handId);
    insureButtonNot.style.visibility = "hidden";

    if(insureTimes == hands){
        var insureButtonsRow =  document.getElementById("insuranceButtons");
        insureButtonsRow.style.display = "none";
        insureTimes = 0;
        showActionButtons();
    }
}

var surrenderTimes = 0;
function surrender(handId) {
    surrenderTimes++;
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "SurrenderBet",
                betPlace: handId
            },
            success: function () {
                var surrenderButton =  document.getElementById("surrender" + handId);
                surrenderButton.style.visibility = "hidden";
                var surrenderButtonNot =  document.getElementById("surrenderNot" + handId);
                surrenderButtonNot.style.visibility = "hidden";

                if(surrenderTimes == hands){
                    var surrenderButtonsRow =  document.getElementById("surrenderButtons");
                    surrenderButtonsRow.style.display = "none";
                    surrenderTimes = 0;
                    showActionButtons();
                }
            }
        });
    });
}

function surrenderNot(handId) {
    surrenderTimes++;
    var surrenderButton =  document.getElementById("surrender" + handId);
    surrenderButton.style.visibility = "hidden";
    var surrenderButtonNot =  document.getElementById("surrenderNot" + handId);
    surrenderButtonNot.style.visibility = "hidden";

    if(surrenderTimes == hands){
        var surrenderButtonsRow =  document.getElementById("surrenderButtons");
        surrenderButtonsRow.style.display = "none";
        surrenderTimes = 0;
        showActionButtons();
    }
}

function showActionButtons() {
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "ShowActionBtn",
            },
            success: function (responseText) {
                $('#gameButtons').html(responseText);
            }
        });
    });
}

function hit(handId) {
    $(document).ready(function () {
        $("#points" + handId).remove();
        $.ajax({
            url: '/Ajax',
            data: {
                command: "HitCard",
                betPlace: handId
            },
            success: function (responseText) {
                $('#cards').append(responseText);
                showActionButtons();
            }
        });
    });
}

function stand(handId) {
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "Stand",
                betPlace: handId
            },
            success: function (responseText) {
                $('#cards').append(responseText);
                showActionButtons();
            }
        });
    });
}

function immediateBjWin(handId) {
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "ImmediateBjWin",
                betPlace: handId
            },
            success: function (responseText) {
                $('#cards').append(responseText);
                showActionButtons();
            }
        });
    });
}

function waitBjWin(handId) {
    $(document).ready(function () {
        $.ajax({
            url: '/Ajax',
            data: {
                command: "Stand",
                betPlace: handId
            },
            success: function (responseText) {
                $('#cards').append(responseText);
                showActionButtons();
            }
        });
    });
}

