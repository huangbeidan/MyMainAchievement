var p1Button = document.querySelector("#p1")
var p2Button = document.getElementById("p2")
var p1Display = document.querySelector("#p1Display")
var p2Display = document.querySelector("#p2Display")
var resetButton = document.querySelector("#reset")
var numInput = document.querySelector("input")
var winnerScoreDisplay = document.querySelector("p span")
var p1Score = 0;
var p2Score = 0;
var gameover = false;
var winningScore = 5;

p1Button.addEventListener("click", function(){
    if (!gameover){
	p1Score++;
	if (p1Score === winningScore){
		p1Display.classList.add("winner");
		gameover = true;
	}
	p1Display.textContent = p1Score;}
})

p2Button.addEventListener("click", function(){
	if (!gameover){
	p2Score++;
	if (p2Score === winningScore){
		p2Display.classList.add("winner");
		gameover = true;
	}
	p2Display.textContent = p2Score;}
})

resetButton.addEventListener("click", function(){
	reset();
})

function reset(){
	p1Score = 0;
	p2Score = 0;
	p1Display.textContent = p1Score;
	p2Display.textContent = p2Score;
	p1Display.classList.remove("winner");
	p2Display.classList.remove("winner");
	gameover = false;
}

numInput.addEventListener("change",function(){
   winnerScoreDisplay.textContent = this.value;
   winningScore = Number(this.value);
   reset();
})