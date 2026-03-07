import axios from 'axios';

async function testLogin() {
  try {
    // 测试前端到后端的请求
    const response = await axios.post('http://localhost:3000/api/auth/login', {
      username: 'test',
      password: 'test123',
      role: 'TOURIST'
    });
    console.log('Login test successful:', response.data);
  } catch (error) {
    console.error('Login test failed:', error.response?.data || error.message);
    console.error('Error config:', error.config);
  }
}

testLogin();