<%@ taglib prefix="chat" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<title>Log In</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/main.css">
<link rel="stylesheet" href="css/loginsignup.css">
</head>
<body>
	<header id="header" class="top-head">
		<chat:header />
	</header>



	<div class="background"></div>
	<div class="main logging">

		<form class="reg" action="signin" method="POST">
					<h3>Login Here</h3>
					<label for="phone">Nick Name</label>
					 
			<input type="text" placeholder="Nick name" id="nickName" name="nickName"> 
					
					<label for="password">Password</label>
					 
			<input type="password" placeholder="Password" id="password" name="password">

					<button>Log In</button>

		</form>
		<div class="social">
			<div class="register">
				<a class="txt2" href="signup"> Sign Up</a>
			</div>
		</div>
	</div>

</body>
</html>