  function openModal(musicId) {
    fetch('http://localhost:8081/api/songs/' + musicId)
      .then(response => {
        if (!response.ok) {
          throw new Error('Não foi possível buscar os detalhes da música.');
        }
        return response.json();
      })
      .then(data => {
        document.getElementById('idInput').value = data.id;
        document.getElementById('nomeInput').value = data.fileName;
        document.getElementById('artistaInput').value = data.artist;
        document.getElementById('duracaoInput').value = data.duration;
        document.getElementById('favoritaCheckbox').checked = data.favorited;
        document.getElementById('urlLink').href = data.urlSong;

        var nomeInput = document.getElementById('nomeInput');
        var artista = document.getElementById('artistaInput');
        var duracaoInput = document.getElementById('duracaoInput');

        // Verifica se o input está desabilitado
        if (nomeInput.disabled) {
          // Define o estilo de fundo para cinza quando o input está desabilitado
          nomeInput.style.backgroundColor = 'silver';
          artista.style.backgroundColor = 'silver';
          duracaoInput.style.backgroundColor = 'silver';
        } else {
          nomeInput.style.backgroundColor = 'transparent';
          artista.style.backgroundColor = 'transparent';
        }

        var modal = document.getElementById('myModal');
        modal.classList.remove('hidden');

        // Fechar a modal ao clicar fora dela
        modal.addEventListener('click', function(event) {
          if (event.target === modal) {
            closeModal();
          }
        });
      })
      .catch(error => {
        console.error('Erro ao buscar os detalhes da música:', error);
      });
  }

  function closeModal() {
    var modal = document.getElementById('myModal');
    modal.classList.add('hidden');
    window.location.href = 'http://localhost:8081/home';
  }

  /* Habilitar edição */
   function toggleInputs() {
    // Obtém o estado atual do checkbox
    var checkbox = document.getElementById('habilitarEdicao');
    var isChecked = checkbox.checked;

    // Inputs a serem habilitados/desabilitados
    var nomeInput = document.getElementById('nomeInput');
    var artistaInput = document.getElementById('artistaInput');
    var favoritaCheckbox = document.getElementById('favoritaCheckbox');

    // Habilita ou desabilita os inputs conforme necessário
    nomeInput.disabled = !isChecked;
    artistaInput.disabled = !isChecked;
    favoritaCheckbox.disabled = !isChecked;

    if (nomeInput.disabled) {
    // Define o estilo de fundo para cinza quando o input está desabilitado
    nomeInput.style.backgroundColor = 'silver';
    artistaInput.style.backgroundColor = 'silver';
    duracaoInput.style.backgroundColor = 'silver';
    } else {
      nomeInput.style.backgroundColor = 'transparent';
      artistaInput.style.backgroundColor = 'transparent';
    }
  }

    function saveChanges() {
    // Obtém os valores dos campos de entrada
    var id = document.getElementById('idInput').value;
    var nome = document.getElementById('nomeInput').value;
    var artista = document.getElementById('artistaInput').value;
    var favorita = document.getElementById('favoritaCheckbox').checked;
    var url = document.getElementById('urlLink').href;

        // Constrói o objeto JSON com os dados dos inputs
    var jsonData = {
      id: id,
      fileName: nome,
      artist: artista,
      favorited: favorita // Converte para booleano
    };

    fetch('http://localhost:8081/api/songs/' + id, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(jsonData)
    })
    .then(response => {
      if (!response.ok) {
        throw new Error('Não foi possível atualizar os dados da música.');
      }
      alert("salvo com sucesso");
      console.log('Dados atualizados com sucesso:', jsonData);
      closeModal();
    })
    .catch(error => {
      console.error('Erro ao atualizar os dados da música:', error);
    });
  }

  function clearFields() {
    document.getElementById('nomeInput').value = "";
    document.getElementById('artistaInput').value = "";
    document.getElementById('favoritaCheckbox').checked = false;
  }

  function downloadFile(url) {
    var filename = url.substring(url.lastIndexOf('/') + 1);
    console.log(url);
    fetch(url)
        .then(response => response.blob())
        .then(blob => {
            const url = window.URL.createObjectURL(new Blob([blob]));
            const a = document.createElement('a');
            a.href = url;
            a.download = filename;
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
        })
        .catch(error => {
            console.error('Erro ao fazer o download do arquivo:', error);
        });
}

  function audioPlay(audioElement) {
     var index = audioElement.getAttribute('data-index');
     console.log('Reproduzindo áudio...', index);
     let wave = document.getElementsByClassName('wave')[index];
     wave.classList.add('active2');
  }

  function audioPause(audioElement) {
     var index = audioElement.getAttribute('data-index');
     let wave = document.getElementsByClassName('wave')[index];

     console.log('Pausando áudio...');
     wave.classList.remove('active2');
  }

  function validateFile() {
        var fileInput = document.getElementById('fileInput');
        var file = fileInput.files[0];

        if (!file) {
            alert('Por favor, selecione um arquivo.');
            return false;
        }

        if (file.type !== 'audio/mpeg') {
            alert('Por favor, selecione um arquivo de áudio MP3.');
            return false;
        }
        return true;
    }