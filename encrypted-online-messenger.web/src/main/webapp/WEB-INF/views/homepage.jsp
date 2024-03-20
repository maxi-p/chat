<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="chat" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Chatting Network</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="css/main.css">
<script src="https://code.jquery.com/jquery-3.7.1.min.js"
	integrity="sha256-/JqT3SQfawRcv/BIHPThkBvs0OEvtFFmqPF/lYI/Cxo="
	crossorigin="anonymous"></script>
<script src="https://kit.fontawesome.com/9e5ba2e3f5.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<header id="header" class="top-head">
		<chat:header />
	</header>
	<main>
		<c:if test="${loggedInUser != null}">
			<aside class="friends-list">
				<div class="header-wrapper">
					<div class="header-container">

						<input class="add-friend-button" id="add-friend-element"
							value="Add" type="submit" />
						<div class="search-box">
							<input autocomplete="off" type="text" id="addFriendField"
								name="users-nickName" placeholder="Add User by Nick Name">
						</div>

					</div>
				</div>
				<div class="body-container">
					<div class="chats" id="friends-j">
						<%--
						Friend CHAT OPTIONS
						 --%>
					</div>
				</div>
			</aside>
			<section class="content">
				<div class="container" id="chatBox">
					<div class="content-header">
						<div class="toggle-button">
							<i class="fas fa-arrow-left"></i>
						</div>
						<div class="details" id="friendsNick">
							<%--
						IF FRIEND-SELECTED
						FRIENDS NICK NAME
						--%>
						</div>
					</div>
					<div class="chat-with-friend-container">

						<div class="chat-with-friend-window" id="messeges-j">
							<%--
							MESSAGES OF THE SELECTED CHAT
							--%>
						</div>
					</div>
					<div class="message-draft-part" id="message-draft">
						<%--
						DRAFT PART
						--%>
					</div>

				</div>
			</section>
		</c:if>
	</main>
	<c:if test="${loggedInUser != null}">
		<script type="text/javascript">
		
		var jsonEndpoint = ${endpoint};
		var wsEndpoint = jsonEndpoint.wsaddress;
		
		const jsonEndpoint2 = window.location+'';
		const jsonEndpoint3 = jsonEndpoint2.replace(/http/, "ws").replace(/homepage/,"");
		console.log(jsonEndpoint3);
		
		
		$(function(){
		    $('.toggle-button').on('click', function(){
		    	var w = window.innerWidth;
		    	if(w < 800){
		    	$('.friends-list').toggleClass('friends-list-min');
		    	$('.content').toggleClass('content-visible');
		    		
		    	}
		    });
		});
		var json;
		var friendshipWithAdmin;
		var msgs;
		var jsonChat;
		
		
		var jsonFriends = ${friends};
		var frnds = jsonFriends.friends;
		
		var loggedUser = jsonFriends.loggeduser;
		
		var websocket3 = new WebSocket(jsonEndpoint3+"chat");
		
		function switchContext(friendNick){
			
			
			console.log("switching context!");
			console.log(friendNick);
			websocket3.send(friendNick);
			
		}
		
		websocket3.onmessage = function processMessage(message){
			var w = window.innerWidth;
			if(w < 800){
				$('.friends-list').toggleClass('friends-list-min');
				$('.content').toggleClass('content-visible');
				
			}
			
			jsonChat = JSON.parse(message.data);
			console.log(jsonChat);
			var friendsNick = $("#friendsNick");
			friendsNick.empty();
			friendsNick.append($("<h3>"+jsonChat.selectedFriend+"</h3>"));
			
			var messageDraft = $("#message-draft");
			messageDraft.empty();
			messageDraft.append($("<div class=\"message-box\"><div class=\"message-content\"><input autocomplete=\"off\" type=\"text\" id=\"messageText\" placeholder=\"Message\"name=\"draft\"></div><input class=\"send-message-button\" onClick=\"sendMessage();\"value=\"Send\" type=\"submit\"></div>"));
			
			var notif = $("#notif-"+jsonChat.selectedFriend);
			notif.remove();
			
			if(jsonChat != null){
				 msgs = jsonChat.messages
				 loggedUser = jsonChat.loggeduser
			}
			
			var messeges = $("#messeges-j");
			messeges.empty();
			if(msgs != null){
			
			$.each(msgs,function(index, value){
				if(value.sender == loggedUser){
					messeges.append($("<div class=\"message-block  my-text\"><div class=\"chat-msg\"><p>"+value.msg+"</p><span class=\"time\">" + value.time+"</span>"));	
				}else{
					messeges.append($("<div class=\"message-block  friends-text\"><div class=\"chat-msg\"><p>"+value.msg+"</p><span class=\"time\">" + value.time+"</span>"));
				}
			});
			}
		}
		

		
		if(json != null){
			 msgs = json.messages
		}
		$(document).ready(function(){
			var messeges = $("#messeges-j");
			if(msgs != null){
			
			$.each(msgs,function(index, value){
				if(value.sender === loggedUser){
					messeges.append($("<div class=\"message-block  my-text\"><div class=\"chat-msg\"><p>"+value.msg+"</p><span class=\"time\">" + value.time+"</span>"));	
				}else{
					messeges.append($("<div class=\"message-block  friends-text\"><div class=\"chat-msg\"><p>"+value.msg+"</p><span class=\"time\">" + value.time+"</span>"));
				}
			});
			}

			var friends = $("#friends-j");
			console.log(friends);
			$.each(frnds,function(index, value){
				if(value.notification > 0){
				friends.append($(" <a onClick=\"switchContext(this.id)\" id=\""+value.nick+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+value.nick+"</h3><span>"+value.lastTime+"</span></div><div class=\"last-message\"><p>"+value.from+": "+value.lastMessage+"</p><span id=\"notif-"+value.nick+"\">"+value.notification+"</span></div></div></div></a>"));	
				
				}
				else{
					friends.append($(" <a onClick=\"switchContext(this.id)\" id=\""+value.nick+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+value.nick+"</h3><span>"+value.lastTime+"</span></div><div class=\"last-message\"><p>"+value.from+": "+value.lastMessage+"</p></div></div></div></a>"));	
				}
			});
			
		});
		var webHash = new Object();
		var myJsonFriends = ${friends};
		var friendsList = myJsonFriends.friends;
		
		const processing = function processMessage(message){
			console.log("GOt a message! from ");
			console.log(message.data);
			var jsonData = JSON.parse(message.data);
			console.log(jsonData.sender);
			var messeges = $("#messeges-j");
			var friends1 = $("#friends-j");
			if(jsonData.msg != null) {
				console.log("refreshing!");
				if(jsonData.sender === loggedUser){
					
					messeges.append($("<div class=\"message-block  my-text\"><div class=\"chat-msg\"><p>"+jsonData.msg+"</p><span class=\"time\">" + jsonData.time+"</span>"));
					var lastM = $("#"+jsonData.receiver);
					lastM.remove();
					if(jsonData.notificationSender > 0){
						friends1.prepend($("<a onClick=\"switchContext(this.id)\" id=\""+jsonData.receiver+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+jsonData.receiver+"</h3><span>"+jsonData.time+"</span></div><div class=\"last-message\"><p>"+jsonData.sender+": "+jsonData.msg+"</p><span id=\"notif-"+value.nick+"\">"+jsonData.notificationSender+"</span></div></div></div></a>"));
					}
					else{
						friends1.prepend($("<a onClick=\"switchContext(this.id)\"  id=\""+jsonData.receiver+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+jsonData.receiver+"</h3><span>"+jsonData.time+"</span></div><div class=\"last-message\"><p>"+jsonData.sender+": "+jsonData.msg+"</p></div></div></div></a>"));
					}
					
				}else{
					if(jsonChat !=null && jsonChat.selectedFriend != null && jsonChat.selectedFriend == jsonData.sender)
					messeges.append($("<div class=\"message-block  friends-text\"><div class=\"chat-msg\"><p>"+jsonData.msg+"</p><span class=\"time\">" + jsonData.time+"</span>"));
					var lastM = $("#"+jsonData.sender);
					lastM.remove();
					if(jsonData.notification > 0){
						friends1.prepend($("<a onClick=\"switchContext(this.id)\"  id=\""+jsonData.sender+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+jsonData.sender+"</h3><span>"+jsonData.time+"</span></div><div class=\"last-message\"><p>"+jsonData.sender+": "+jsonData.msg+"</p><span id=\"notif-"+jsonData.sender+"\">"+jsonData.notification+"</span></div></div></div></a>"));
					}
					else{
						friends1.prepend($("<a onClick=\"switchContext(this.id)\"  id=\""+jsonData.sender+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+jsonData.sender+"</h3><span>"+jsonData.time+"</span></div><div class=\"last-message\"><p>"+jsonData.sender+": "+jsonData.msg+"</p></div></div></div></a>"));
					}
				}
			}
		};
		
		friendsList.forEach((value) => {
			webHash[value.friendshipId] = new WebSocket(jsonEndpoint3+"socket/"+value.friendshipId);
			console.log("connecting to "+value.friendshipId);
			webHash[value.friendshipId].onmessage = processing;
		});
		
		function sendMessage(){
			console.log("sending a message!");
			if(messageText.value !== "")
			webHash[jsonChat.friendshipId].send(messageText.value);
			messageText.value="";
		}

	var websocket2 = new WebSocket(jsonEndpoint3+"addFriendSocket");
	console.log("connecting to friends additions listener.");
	websocket2.onmessage = function processMessage(message){
		var jsonData = JSON.parse(message.data);
		var friends = $("#friends-j");
		if(jsonData.nick != null) {
			console.log("refreshing!");
			friends.prepend($("<a onClick=\"switchContext(this.id);\" id=\""+jsonData.nick+"\"><div class=\"chat-box\"><div class=\"chat-container\"><div class=\"friend-line\"><h3>"+jsonData.nick+"</h3><span>"+jsonData.time+"</span></div><div class=\"last-message\"><p>: </p></div></div></div></a>"));	
			webHash[jsonData.friendshipId] = new WebSocket(jsonEndpoint3+"socket/"+jsonData.friendshipId);
			console.log("connecting to "+jsonData.friendshipId);
			webHash[jsonData.friendshipId].onmessage = processing;
			if(jsonData.nick === "admin"){
				friendshipWithAdmin = jsonData.friendshipId;
			}
		}
	}
	
	function addFriend(){
		console.log("adding a friend!");
		websocket2.send(addFriendField.value);
		addFriendField.value="";
	}
	
	document.getElementById ("add-friend-element").addEventListener ("click", addFriend, false);
	</script>
		<c:if test="${firstTimeUser != null}">

			<script>
		// vv Greeting!
	
		function customDelay(ms) {
    	return new Promise(resolve => setTimeout(resolve, ms));
		}
	
		var firstTime = ${firstTimeUser};
		var isFirstLog;
		if(firstTime !=null){
			isFirstLog = firstTime.isanewuser;
		}
		if(isFirstLog!=null && isFirstLog === "yes!"){
			adminCall();
			
		}
	
		async function adminCall() {
		await customDelay(1000);
		websocket2.send("vluafdihsgfjm;vHCJASNCVIASCN_!(OCIDCNJZNELVr8yp2*&Y&*&SDVJESLAWD@!(&$@(#8417foaeltrurjw852qi0(*hnL&&^F#&I*F@ECSJZSfqvnlsnd23895dbkjanaks))))");
		console.log("sent message to admin");
		await customDelay(1500);
		webHash[friendshipWithAdmin].send("vluafdihsgfjm;vHCJASNCVIASCN_!(OCIDCNJZNELVr8yp2*&Y&*&SDVJESLAWD@!(&$@(#8417foaeltrurjw852qi0(*hnL&&^F#&I*F@ECSJZSfqvnlsnd23895dbkjanaks))))"+"Wecome to Chat App by Maksim Petrushin!");
		await customDelay(4000);
		webHash[friendshipWithAdmin].send("vluafdihsgfjm;vHCJASNCVIASCN_!(OCIDCNJZNELVr8yp2*&Y&*&SDVJESLAWD@!(&$@(#8417foaeltrurjw852qi0(*hnL&&^F#&I*F@ECSJZSfqvnlsnd23895dbkjanaks))))"+"Add Maksim to friends! His nick-name is \"max\" ");
	}
	// ^^ Greeting!
	</script>
		</c:if>
	</c:if>
</body>
</html>


