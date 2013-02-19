
curId = 1

actionOnEnterKey = (event) ->
  if event.keyCode is 13
    ingredientKey = $("#ingredientToAdd").get(0).value
    majIngredientLine ingredientKey, curId
    curId = curId + 1
    
    
majIngredientLine = (ingredientKey, lineId) ->
  url = "/ingredient/" + ingredientKey + "/apports.json"
  $.ajax
    url: url
    dataType: "json"
    success: (data) ->
      newInputFiledIngredient lineId
      updateCalcIngredientLine data, lineId
      updateCalcTotal()

newInputFiledIngredient = (id) ->
  res = "<tr id=\"ingredient" + id + "\">             <td class=\"id\">id" + id + "</td>             <td class=\"quantity\"><input type=\"text\" class=\"quantity\" size=\"5\" value=\"100\"/> gr</td>             <td class=\"ingredientName\">nom</td>             <td class=\"family\">famille</td>             <td class=\"glucides\">glucides</td>             <td class=\"lipides\">lipides</td>             <td class=\"proteines\">protéines</td>             <td class=\"glucidesTt\">glucides</td>             <td class=\"lipidesTt\">lipides</td>             <td class=\"proteinesTt\">protéines</td>             </tr>"
  $("#listIngredients > tbody").append res
  addOnChangeInputField id

addOnChangeInputField = (id) ->
  ingredientId = "ingredient" + id
  $("tbody > tr#" + ingredientId + " > td > input").keyup ->
    updateCalcIngredientLineNutriments id
    updateCalcTotal()

updateCalcIngredientLineNutriments = (id) ->
  ingredientId = "ingredient" + id
  glucidesStr = $("tbody > tr#" + ingredientId + " > td.glucides").get(0).firstChild.nodeValue
  glucides = parseFloat(glucidesStr)
  proteinesStr = $("tbody > tr#" + ingredientId + " > td.proteines").get(0).firstChild.nodeValue
  proteines = parseFloat(proteinesStr)
  lipidesStr = $("tbody > tr#" + ingredientId + " > td.lipides").get(0).firstChild.nodeValue
  lipides = parseFloat(lipidesStr)
  quantityStr = $("tbody > tr#" + ingredientId + " > td > input").get(0).value
  quantity = parseFloat(quantityStr)
  $("tr#" + ingredientId + " > td.glucidesTt").get(0).firstChild.nodeValue = (glucides * quantity) / 100
  $("tr#" + ingredientId + " > td.lipidesTt").get(0).firstChild.nodeValue = (lipides * quantity) / 100
  $("tr#" + ingredientId + " > td.proteinesTt").get(0).firstChild.nodeValue = (proteines * quantity) / 100

updateCalcIngredientLine = (ingredientObj, lineId) ->
  ingredientId = "ingredient" + lineId
  $("tr#" + ingredientId + " > td.id").get(0).firstChild.nodeValue = ingredientObj.id
  $("tr#" + ingredientId + " > td.ingredientName").get(0).firstChild.nodeValue = ingredientObj.name
  $("tr#" + ingredientId + " > td.family").get(0).firstChild.nodeValue = ingredientObj.famille
  $("tr#" + ingredientId + " > td.glucides").get(0).firstChild.nodeValue = ingredientObj.glucides
  $("tr#" + ingredientId + " > td.lipides").get(0).firstChild.nodeValue = ingredientObj.lipides
  $("tr#" + ingredientId + " > td.proteines").get(0).firstChild.nodeValue = ingredientObj.proteines

updateCalcTotal = ->
  glucides = calcColumn("tbody > tr > td.glucides")
  proteines = calcColumn("tbody > tr > td.proteines")
  lipides = calcColumn("tbody > tr > td.lipides")
  glucidesTt = calcColumn("tbody > tr > td.glucidesTt")
  proteinesTt = calcColumn("tbody > tr > td.proteinesTt")
  lipidesTt = calcColumn("tbody > tr > td.lipidesTt")
  $("tfoot > tr > td.glucides").get(0).firstChild.nodeValue = glucides
  $("tfoot > tr > td.proteines").get(0).firstChild.nodeValue = proteines
  $("tfoot > tr > td.lipides").get(0).firstChild.nodeValue = lipides
  $("tfoot > tr > td.glucidesTt").get(0).firstChild.nodeValue = glucidesTt
  $("tfoot > tr > td.proteinesTt").get(0).firstChild.nodeValue = proteinesTt
  $("tfoot > tr > td.lipidesTt").get(0).firstChild.nodeValue = lipidesTt

calcColumn = (columnName) ->
  res = 0.0
  $(columnName).each (index, value) ->
    currentVal = parseFloat(value.firstChild.nodeValue)
    res += currentVal  if currentVal > 0.0

  res.toFixed 2
  
$ ->
  $("#ingredientToAdd").autocomplete
    source: "http://localhost:9000/ingredients/startwith.json"
    minLength: 3


$ ->
  $("#ingredientToAdd").keyup actionOnEnterKey
  