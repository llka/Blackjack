function validateForm() {
    var result = true;
    // const
    var PWD_NOT_EQUAL = "Passwords do not match";

    var errPwd1 =  document.getElementById("err-pwd1"),
        errPwd2 =  document.getElementById("err-pwd2");
    errPwd1.style.visibility = "hidden";
    errPwd2.style.visibility = "hidden";
    errPwd1.innerHTML = "";
    errPwd2.innerHTML = "";

    var	pwd1 = document.forms["profileInfoForm"]["password"].value,
        pwd2 = document.forms["profileInfoForm"]["confPassword"].value;

    if (pwd1 && pwd2 && pwd1 !== pwd2) {
        errPwd1.style.visibility = "visible";
        errPwd2.style.visibility = "visible";
        errPwd1.innerHTML = PWD_NOT_EQUAL;
        errPwd2.innerHTML = PWD_NOT_EQUAL;
        document.forms["profileInfoForm"]["password"].value = "";
        document.forms["profileInfoForm"]["confPassword"].value = "";
        result = false;
    }
    return result;
}
function showPassword() {
    var passSpan1 =  document.getElementById("passSpan1"),
        passSpan2 =  document.getElementById("passSpan2"),
        buttonPassword =  document.getElementById("buttonPassword");

    passSpan1.style.display="block";
    passSpan2.style.display="block";
    buttonPassword.style.display="none";

    var input = document.createElement("INPUT");
    var attType = document.createAttribute("type");
    attType.value = "password";
    var attName = document.createAttribute("name");
    attName.value = "password";
    var attRegex = document.createAttribute("pattern");
    attRegex.value="(?=^.{6,50}$)(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$";
    var attTitle = document.createAttribute("title");
    attTitle.value = "Should be at least 6 symbols long and must consist of lowercase and uppercase Latin letters, numbers";
    var attReq = document.createAttribute("required");
    input.setAttributeNode(attRegex);
    input.setAttributeNode(attReq);
    input.setAttributeNode(attType);
    input.setAttributeNode(attName);
    input.setAttributeNode(attTitle);
    document.getElementById("password1").appendChild(input);

    var input2 = document.createElement("INPUT");
    var attType2 = document.createAttribute("type");
    attType2.value = "password";
    var attName2 = document.createAttribute("name");
    attName2.value = "confPassword";
    var attRegex2 = document.createAttribute("pattern");
    attRegex2.value="(?=^.{6,50}$)(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$";
    var attTitle2 = document.createAttribute("title");
    attTitle2.value = "Repeat your password";
    var attReq2 = document.createAttribute("required");
    input2.setAttributeNode(attReq2);
    input2.setAttributeNode(attType2);
    input2.setAttributeNode(attRegex2);
    input2.setAttributeNode(attName2);
    input2.setAttributeNode(attTitle2);
    document.getElementById("password2").appendChild(input2);
}

function showBalanceAdd() {
    var amountText  =  document.getElementById("amountText"),
        amount =  document.getElementById("amount");
    var show =  document.getElementById("show");
    var sub =  document.getElementById("sub");

    amount.style.display="block";
    amountText.style.display="block";

    var attType3 = document.createAttribute("type");
    attType3.value = "submit";
    show.setAttributeNode(attType3);
    show.removeAttribute("onclick");
}
