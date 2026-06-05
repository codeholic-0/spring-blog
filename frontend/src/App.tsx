import { Routes, Route, Link } from "react-router-dom";
import PostList from "./components/PostList";
import PostForm from "./components/PostForm";
import PostDetail from "./components/PostDetail";

function App() {
	return (
		<div className="min-h-screen bg-gray-50">
			<nav className="bg-white shadow-sm border-b">
				<div className="max-w-4xl mx-auto px-4 py-3 flex justify-between items-center">
					<Link to="/" className="text-xl font-bold text-gray-800">
						Blog Platform
					</Link>
					<Link
						to="/posts/new"
						className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 text-sm"
					>
						New Post
					</Link>
				</div>
			</nav>

			<main className="max-w-4xl mx-auto px-4 py-6">
				<Routes>
					<Route path="/" element={<PostList />} />
					<Route path="/posts/new" element={<PostForm />} />
					<Route path="/posts/:id" element={<PostDetail />} />
					<Route path="/posts/:id/edit" element={<PostForm />} />
				</Routes>
			</main>
		</div>
	);
}

export default App;
