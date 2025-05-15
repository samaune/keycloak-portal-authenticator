
const wordpress_login = async (username, password, ) => {
  const tokenUrl = `https://www.singha.local/wp-json/jwt-auth/v1/token`;


  const params = new URLSearchParams();
  params.append('username', username);
  params.append('password', password);
  try {
    //const response = await axios.post(tokenUrl, params, {
    const response = await fetch(tokenUrl, {
      agent: agent,
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: {
        username: username,
        password: password,
      },
    });
    const data = await response.json();
    console.log('Access Token:', data.access_token);
    return data.access_token;
  } catch (error) {
    console.log(error);
//    console.error('Error fetching token:', error.response?.data || error.message);
  }
};

const username = '';
const password = '';
wordpress_login(username, password);