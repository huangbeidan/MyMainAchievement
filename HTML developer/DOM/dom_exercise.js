var button = document.querySelector("button");

button.addEventListener("click",function(){
	// if(isPurple){
	// 	document.body.style.background = "white";
		
	// } else {
	// 	document.body.style.background = "purple";
	// 	isPurple = true;
	// }
	// isPurple = !isPurple;
	document.body.classList.toggle("purple");
});

// toggle is a function,
// if there is a class called purple, it will remove it,
// 	otherwise, add it!