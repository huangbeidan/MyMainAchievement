
var numSquares = 6;
var colors = [];
var pickedColor;
var squares = document.querySelectorAll(".square");
var messageDisplay = document.querySelector("#message")
var h1 = document.querySelector("h1");
var resetButton = document.querySelector("#reset");
var modeButtons = document.querySelectorAll(".mode");
var colorDisplay = document.getElementById("colorDisplay");

init();

function init(){
	// mode button event listeners
    setUpModeButtons();
    setupSquares();
    reset();
}

resetButton.addEventListener("click", function(){
    reset();
})


function setupSquares(){
	for (var i=0; i<squares.length;i++){
	// add initial colors to squares
	// add click listeners to squares
	squares[i].addEventListener("click",function(){

		var clickedcolor = this.style.background;

		if(clickedcolor === pickedColor){
			messageDisplay.textContent = "Correct!"
			resetButton.textContent = "Play Again!"
			changeColors(clickedcolor);
			h1.style.background = clickedcolor;
			
		} else {
			this.style.background = "#232323";
			messageDisplay.textContent = "Try Again"
		}
	});
   }
}


function setUpModeButtons(){

	for(var i=0; i<modeButtons.length; i++){
		modeButtons[i].addEventListener("click", function(){
			modeButtons[0].classList.remove("selected");
			modeButtons[1].classList.remove("selected");
			this.classList.add("selected");
			this.textContent === "Easy"? numSquares = 3: numSquares = 6;
			reset();
		});
	}
}

function reset(){
	colors = generateRandomColors(numSquares);
	pickedColor = pickColor();
	colorDisplay.textContent = pickedColor;
	resetButton.textContent = "New Colors"
	messageDisplay.textContent = "";
    for (var i=0;i<squares.length;i++){
    	if (colors[i]){
    		squares[i].style.display = "block"
    		squares[i].style.background = colors[i];
    	} else{
    		squares[i].style.display = "none";
    	}	
	}
	h1.style.background = "steelblue";
}


function changeColors(color){
	for (var i=0;i<colors.length;i++){
		squares[i].style.background = color;
	}
}

function pickColor(){
	var random = Math.floor(Math.random() * colors.length);
	return colors[random];
}

function generateRandomColors(num){
	var arr = []
	for (var i=0;i<num;i++){
		arr.push(randomColor())
		//get random color and push into array
	}
	return arr;
}

function randomColor(){
	//pick a "red" from 0-255
	var r = Math.floor(Math.random()*256)
	//pick a "blue" from 0-255
	var g = Math.floor(Math.random()*256)
	//pick a "green" from 0-255
	var b = Math.floor(Math.random()*256)
	return "rgb(" +r + ", " + g + ", " + b + ")"
}