//process.env.NODE_TLS_REJECT_UNAUTHORIZED = "0";


const keycloak_login = async (username, password, ) => {
  const tokenUrl = `http://localhost:8080/realms/staging/protocol/openid-connect/token`;

  const params = new URLSearchParams();
  params.append('grant_type', 'password');
  params.append('client_id', 'portal');
  params.append('client_secret', '');
  params.append('username', username);
  params.append('password', password);
  try {
    //const response = await axios.post(tokenUrl, params, {
    const response = await fetch(tokenUrl, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: params,
    });
    const data = await response.json();
    console.log('Access Token:', data.access_token);
    return data.access_token;
  } catch (error) {
    console.error(error);
  }
};

const username = '';
const password = '';
keycloak_login(username, password);