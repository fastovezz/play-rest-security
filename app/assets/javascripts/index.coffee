$ ->
  init()


init = () ->
  if (!localStorage.getItem("authToken"))
    displayLoginForm()
  else
    displayFavlists()


displayLoginForm = () ->
  $("body").empty()
  loginForm = $("<form>").attr("action", "/login").attr("method", "post").attr("id", "loginForm")
  loginForm.append $("<input>").attr("id", "emailAddress").attr("name", "emailAddress").val("user1@demo.com")
  loginForm.append $("<input>").attr("id", "password").attr("name", "password").attr("type", "password").val("password")
  loginForm.append $("<input>").attr("type", "submit").val("Login")
  loginForm.submit (event) ->
    event.preventDefault()
    $.ajax
      url: event.currentTarget.action
      type: event.currentTarget.method
      dataType: 'json'
      contentType: 'application/json'
      data: JSON.stringify({emailAddress: $("#emailAddress").val(), password: $("#password").val()})
      error: (jqXHR, errorText, error) ->
        displayError("Login failed")
      success: doLogin
  $("body").append loginForm


doLogin = (data, textStatus, jqXHR) ->
  console.log(data)
  # global state holder for the auth token
  localStorage.setItem("authToken", data.authToken)
  $("#loginForm").remove()
  displayFavlists()


displayFavlists = () ->
  fetchFavlists()
  fetchMovies()
  $("body").empty()
  $("body").append $("<button>").text("Logout").click(doLogout)
  $("body").append $("<h3>").text "Your Favlists"
  favList = $("<ul>").attr("id", "favlists")
  $("body").append favList
  moviesList = $("<ul>").attr("id", "movies")
  $("body").append moviesList
  favlistForm = $("<form>").attr("action", "/favlists").attr("method", "post").attr("id", "favlistForm")
  favlistForm.append $("<input>").attr("id", "favlistValue").attr("name", "value").attr("required", true)
  favlistForm.append $("<input>").attr("type", "submit").val("Create Favlist")
  favlistForm.submit(createFavlist)
  $("body").append favlistForm


createFavlist = (event) ->
  event.preventDefault()
  $.ajax
    url: event.currentTarget.action
    type: event.currentTarget.method
    dataType: 'json'
    contentType: 'application/json'
    headers: {"X-AUTH-TOKEN": localStorage.getItem("authToken")}
    data: JSON.stringify({name: $("#favlistValue").val()})
    error: (jqXHR, errorText, error) ->
      if (jqXHR.status == 401)
        displayLoginForm()
      else if (JSON.parse(jqXHR.responseText).value[0] != undefined)
        displayError("A value must be specified for the Favlist")
      else
        displayError("An uknown error occurred")
    success: fetchFavlists


fetchFavlists = () ->
  $.ajax
    url: "/favlists"
    type: "get"
    dataType: 'json'
    headers: {"X-AUTH-TOKEN": localStorage.getItem("authToken")}
    error: (jqXHR, errorMessage, error) ->
      if (jqXHR.status == 401)
        displayLoginForm()
    success: (favlists) ->
      $("#favlistValue").val("")
      favList = $("#favlists")
      favList.empty()
      $.each favlists, (index, favlist) ->
        favList.append $("<li>").text(favlist.name)

fetchMovies = () ->
  $.ajax
    url: "/movies"
    type: "get"
    headers: {"X-AUTH-TOKEN": localStorage.getItem("authToken")}
    data: {searchText: "batman"}
    error: (jqXHR, errorMessage, error) ->
      if (jqXHR.status == 401)
        displayLoginForm()
    success: (results) ->
      moviesList = $("#movies")
      moviesList.empty()
      $.each results, (key, result) ->
        if(key == "results")
          result.sort (val1, val2) ->
            val1.release_date.localeCompare(val2.release_date)
          $.each result, (innerKey, certainResult) ->
            liItem = $("<li>")
            liItem.text(certainResult.title + " (" + certainResult.release_date + ")")
            poster = $("<img>", {
              src: "http://image.tmdb.org/t/p/w300/" + certainResult.poster_path,
              width: 60
            })
            poster.css( "padding-right", "15px" )
            liItem.prepend poster
            moviesList.append liItem
            console.log("movie " + innerKey + ": " + JSON.stringify(certainResult))


displayError = (error) ->
  $("body").prepend $("<span>").text(error).css("color", "red")


doLogout = (event) ->
  $.ajax
    url: "/logout"
    type: "post"
    dataType: 'json'
    headers: {"X-AUTH-TOKEN": localStorage.getItem("authToken")}
    success: (data) ->
      console.log(data)
      localStorage.removeItem("authToken")
      displayLoginForm()
    error: (data) ->
      console.error(data)
      localStorage.removeItem("authToken")
      displayLoginForm()
