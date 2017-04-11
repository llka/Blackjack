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