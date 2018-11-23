var movies_note = [
    {
    name: "In Bruges",
     rating: "5 stars",
     wathced: false
     },
     {
    name: "Frozen",
         rating: "4.5 stars",
         wathced: false
     },
    {
        name: "Mad Max Fury Road",
        rating: "5 stars",
        watched: true
    },
    {
        name: "Les Miserables",
        rating: "3.5 stars",
        watched: false
    }]

    movies_note.forEach(function(movie){
    if (movie.wathced == false){
        console.log("You have not seen " + movie.name + " - " + movie.rating)}
    else {
        console.log("You have seen " + movie.name + " - " + movie.rating)}
})

    # A better way

    function buildString(movie){
    var result = "You have ";
    if (movie.watched){
        result += "watched ";
    } else {
        result += "not seen ";
    }
    result += movie.name + " - " + movie.rating;
    return result;
}

movies_note.forEach(function(movie){
    console.log(buildString(movie));});

    