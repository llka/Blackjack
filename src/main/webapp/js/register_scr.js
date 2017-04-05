var flagRegister = false;
function validateForm() {
var result = true;
    // const
    flagRegister = false;
  var PWD_NOT_EQUAL = "Passwords do not match";

  var errPwd1 =  document.getElementById("err-pwd1"),
    errPwd2 =  document.getElementById("err-pwd2");
    errPwd1.style.visibility = "hidden";
    errPwd2.style.visibility = "hidden";
  errPwd1.innerHTML = "";
  errPwd2.innerHTML = "";

  var	pwd1 = document.forms["registerForm"]["password"].value,
      pwd2 = document.forms["registerForm"]["confPassword"].value;

  if (pwd1 && pwd2 && pwd1 !== pwd2) {
      errPwd1.style.visibility = "visible";
      errPwd2.style.visibility = "visible";
      errPwd1.innerHTML = PWD_NOT_EQUAL;
      errPwd2.innerHTML = PWD_NOT_EQUAL;
      document.forms["registerForm"]["password"].value = "";
      document.forms["registerForm"]["confPassword"].value = "";
    result = false;
    flagRegister = true;
  }
  showRegister();
  return result;
}

function showRegister(){
    if(flagRegister){
        document.getElementById('id02').style.display='block';
    }else {
        document.getElementById('id02').style.display='none';
    }
}

var flag = true;

function addCode(){
    if(flag){
        var text = document.createElement("P");
        var Ptext = document.createTextNode("Invite code");
        text.appendChild(Ptext);
        var attTextId = document.createAttribute("id");
        attTextId.value = "addCodeTextId";
        text.setAttributeNode(attTextId);
        document.getElementById("addCodeText").appendChild(text);

        var input = document.createElement("INPUT");
        var attType = document.createAttribute("type");
        attType.value = "text";
        var attName = document.createAttribute("name");
        attName.value = "inviteCode";
        var attRegex = document.createAttribute("pattern");
        attRegex.value = "[0-9A-Z]{4}[-]{1}[0-9A-Z]{4}$";
        var attTitle = document.createAttribute("title");
        attTitle.value = "Enter your real invite code";
        var attInputId = document.createAttribute("id");
        attInputId.value = "addCodeInputId";
        input.setAttributeNode(attInputId);
        input.setAttributeNode(attType);
        input.setAttributeNode(attName);
        input.setAttributeNode(attRegex);
        input.setAttributeNode(attTitle);
        document.getElementById("addCodeInput").appendChild(input);
        flag = false;
    }else {
        var parentText = document.getElementById("addCodeText");
        var childText = document.getElementById("addCodeTextId");
        parentText.removeChild(childText);

        var parentInput = document.getElementById("addCodeInput");
        var childInput = document.getElementById("addCodeInputId");
        parentInput.removeChild(childInput);
        flag = true;
    }
}
function closeModal(){
    // Get the modal
    var modal1 = document.getElementById('id01');
    var modal2 = document.getElementById('id02');

    // When the user clicks anywhere outside of the modal, close it
    window.onclick = function(event) {
        if (event.target == modal1 || event.target == modal2) {
            modal1.style.display = "none";
            modal2.style.display = "none";
        }
    }
}
