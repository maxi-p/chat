<%@ taglib prefix="shop" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="chat" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Sign Up</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/loginsignup.css">

</head>
<body>
	<header id="header" class="top-head">
		<chat:header />
	</header>

	<main>

	<div class="main registering">

		<form class="reg" action="signup" method="POST">
			<h3>Register Here</h3>



			         																							<label class="registering-label" for="nickName">Nick Name</label> 
			<input type="text" placeholder="Nick Name" id="nickName" name="nickName">                         <label class="registering-label"  for="password">Password</label>
			<input type="password" placeholder="Password" id="password" name="password">                      <label class="registering-label"  for="password">Repeat Password</label> 
			<input type="password" placeholder="Repeat Password" id="repeatPassword" name="repeatPassword">





				<button>Register</button>
			<c:if test="${errMsg != null}">
				<div>
					<span class="txtErr">${errMsg}</span>
				</div>
			</c:if>
			<c:remove var="errMsg" />
		</form>
		<div class="social">
			<div class="register">
				<a class="txt2" href="signin"> Log In</a>
			</div>
		</div>
	</div>

		</main>
</body>
</html>