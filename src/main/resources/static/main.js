var lobbyInfoForm = document.querySelector('#lobbyInfoForm');
var joiningLobbyForm = document.querySelector('#joiningLobbyForm');
var creatingLobbyForm = document.querySelector('#creatingLobbyForm');
var connectingElement = document.querySelector('.connecting');
var lobbyInfoLabel = document.querySelector('#lobbyInfoLabel');
var lobbyPlayersInfoLabel = document.querySelector('#lobbyPlayersInfoLabel');

var stompClient = null;
var username = null;
var lobbyId = null;

function join(event) {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    username = document.querySelector('#login').value.trim();
    lobbyId = document.querySelector('#lobbyId').value.trim();
    if (username) {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onJoined, onConnectedError);
    }
    event.preventDefault();
}

function create(event) {
    if (stompClient != null) {
        stompClient.disconnect();
    }
    username = document.querySelector('#loginC').value.trim();
    lobbyId = document.querySelector('#lobbyIdC').value.trim();
    if (username) {

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, onCreated, onConnectedError);
    }
    event.preventDefault();
}

function leave() {
    stompClient.disconnect();
    stompClient = null;
    joiningLobbyForm.classList.remove('hidden');
    creatingLobbyForm.classList.remove('hidden');
    lobbyInfoForm.classList.add('hidden');
}

function onError(payload) {
    console.log('Error: ' + payload.body);
    stompClient.disconnect();
    // stompClient = null;
}

function onCreated() {
    stompClient.subscribe('/user/queue/errors', onError);
    stompClient.subscribe('/topic/public/' + lobbyId, onMessageReceived);

    stompClient.send("/app/lobby.create",
        {},
        JSON.stringify({username: username, lobbyId: lobbyId})
    )
    connectingElement.classList.add('hidden');
}

function onJoined() {
    stompClient.subscribe('/user/queue/errors', onError);
    stompClient.subscribe('/topic/public/' + lobbyId, onMessageReceived);

    stompClient.send("/app/lobby.join",
        {},
        JSON.stringify({username: username, lobbyId: lobbyId})
    )
    connectingElement.classList.add('hidden');
}

function onConnectedError(error) {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}

function onMessageReceived(payload) {
    var lobby = JSON.parse(payload.body);
    joiningLobbyForm.classList.add('hidden');
    creatingLobbyForm.classList.add('hidden');
    lobbyInfoForm.classList.remove('hidden');
    lobbyInfoLabel.textContent = 'lobby id: ' + lobby.id;
    if (lobby.players.length > 0) {
        lobbyPlayersInfoLabel.textContent = 'Players:';
    }
    for (let i = 0; i < lobby.players.length; i++) {
        lobbyPlayersInfoLabel.textContent += ' ' + lobby.players[i].username;
    }
}

joiningLobbyForm.addEventListener('submit', join, true)
creatingLobbyForm.addEventListener('submit', create, true)
lobbyInfoForm.addEventListener('submit', leave, true)
