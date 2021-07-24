import firebase from 'firebase/app';
import 'firebase/auth';

export function initializeFirebase() {
	if (firebase.apps.length === 0) {
		var firebaseConfig = {
			apiKey: 'AIzaSyCykbKgBSkPtdVhsPShv59Lb5hMgWkwa6s',
			authDomain: 'train-station-app.firebaseapp.com',
			projectId: 'train-station-app',
			storageBucket: 'train-station-app.appspot.com',
			messagingSenderId: '1063974850049',
			appId: '1:1063974850049:web:e2704ece9975248ea4ba00'
		};
		firebase.initializeApp(firebaseConfig);
	}
}
