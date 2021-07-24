import 'firebase/auth';

import firebase from 'firebase';

export function initializeFirebase(): void {
	if (firebase.apps.length === 0) {
		const firebaseConfig = {
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
