// ============================================================================
//  ANALISTA FINANCEIRO - INTERFACE COM REACT (VERSÃO ROBUSTA)
// ============================================================================
//  CORREÇÃO: As dependências 'lucide-react' e 'framer-motion' foram removidas
//  e substituídas por SVGs e animações CSS para evitar erros de carregamento.
// ============================================================================

const { useState, useEffect } = React;

// --- Configuração da API ---
const API_BASE_URL = "http://localhost:8080/api";

// --- Cores para o Gráfico ---
const COLORS = [
  '#3b82f6', '#8b5cf6', '#10b981', '#ef4444', '#f97316',
  '#ec4899', '#6366f1', '#f59e0b', '#14b8a6', '#d946ef'
];

// --- Componente de Ícones SVG ---
const Icons = {
  BarChart2: () => <><line x1="12" x2="12" y1="20" y2="10" /><line x1="18" x2="18" y1="20" y2="4" /><line x1="6" x2="6" y1="20" y2="16" /></>,
  UploadCloud: () => <><path d="M4 14.899A7 7 0 1 1 15.71 8h1.79a4.5 4.5 0 0 1 2.5 8.242" /><path d="M12 12v9" /><path d="m16 16-4-4-4 4" /></>,
  AlertTriangle: () => <><path d="m21.73 18-8-14a2 2 0 0 0-3.46 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3Z" /><path d="M12 9v4" /><path d="M12 17h.01" /></>,
  AlertCircle: () => <><circle cx="12" cy="12" r="10" /><path d="M12 8v4" /><path d="M12 16h.01" /></>,
  ArrowLeft: () => <><path d="m12 19-7-7 7-7" /><path d="M19 12H5" /></>,
  FileText: () => <><path d="M15 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7Z" /><path d="M14 2v4a2 2 0 0 0 2 2h4" /><path d="M16 13H8" /><path d="M16 17H8" /><path d="M10 9H8" /></>,
};

const Icon = ({ name, className, size = 24 }) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    width={size}
    height={size}
    viewBox="0 0 24 24"
    fill="none"
    stroke="currentColor"
    strokeWidth="2"
    strokeLinecap="round"
    strokeLinejoin="round"
    className={className}
  >
    {Icons[name] ? Icons[name]() : null}
  </svg>
);


/**
 * Componente Principal da Aplicação
 */
function App() {
  const [page, setPage] = useState('upload');
  const [resumo, setResumo] = useState([]);
  const [transacoes, setTransacoes] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [fileName, setFileName] = useState('');
  const [libsLoaded, setLibsLoaded] = useState(false);
  const [loadError, setLoadError] = useState(null);

  useEffect(() => {
    const checkLibs = () => {
      if (window.Recharts) {
        setLibsLoaded(true);
      }
    };
    
    const intervalId = setInterval(checkLibs, 100);

    const timeoutId = setTimeout(() => {
      if (!libsLoaded) {
        clearInterval(intervalId);
        setLoadError("Falha ao carregar as bibliotecas da interface. Verifique a sua conexão à internet e recarregue a página.");
      }
    }, 5000);
    
    return () => {
      clearInterval(intervalId);
      clearTimeout(timeoutId);
    };
  }, []);

  if (loadError) {
    return (
        <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4">
            <div className="bg-red-900/50 text-red-300 p-6 rounded-md flex flex-col items-center text-center max-w-lg">
                <Icon name="AlertTriangle" className="mb-4 text-red-400" size={48} />
                <h2 className="text-xl font-bold mb-2">Erro de Carregamento</h2>
                <p>{loadError}</p>
            </div>
        </div>
    );
  }

  if (!libsLoaded) {
    return <div className="text-white text-center p-8">A carregar interface...</div>;
  }

  const handleUpload = async (file) => {
    if (!file) return;

    setIsLoading(true);
    setError(null);
    setFileName(file.name);

    const formData = new FormData();
    formData.append('file', file);

    try {
      const uploadResponse = await fetch(`${API_BASE_URL}/transacoes/upload`, {
        method: 'POST',
        body: formData,
      });

      if (!uploadResponse.ok) {
        throw new Error(`Erro no upload: ${uploadResponse.statusText}`);
      }
      
      await fetchData();
      setPage('dashboard');

    } catch (err) {
      console.error("Falha na operação:", err);
      setError("Não foi possível processar o extrato. Verifique se o backend está a correr e se o ficheiro é um PDF válido.");
    } finally {
      setIsLoading(false);
    }
  };
  
  const fetchData = async () => {
      const [resumoResponse, transacoesResponse] = await Promise.all([
          fetch(`${API_BASE_URL}/transacoes/resumo`),
          fetch(`${API_BASE_URL}/transacoes`)
      ]);

      if (!resumoResponse.ok || !transacoesResponse.ok) {
          throw new Error('Falha ao buscar dados do servidor.');
      }

      const resumoData = await resumoResponse.json();
      const transacoesData = await transacoesResponse.json();
      
      setResumo(resumoData);
      setTransacoes(transacoesData);
  }

  const handleBackToUpload = () => {
    setPage('upload');
    setError(null);
    setFileName('');
    setResumo([]);
    setTransacoes([]);
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4">
      <header className="absolute top-0 left-0 w-full p-4 flex items-center justify-center">
         <Icon name="BarChart2" className="text-blue-400 mr-3" size={32} />
         <h1 className="text-3xl font-bold text-gray-200 tracking-tight">Meu Analista Financeiro</h1>
      </header>
      
      <main className="w-full max-w-4xl">
          {page === 'upload' && (
            <UploadPage onUpload={handleUpload} isLoading={isLoading} error={error} fileName={fileName} />
          )}

          {page === 'dashboard' && (
            <DashboardPage resumo={resumo} transacoes={transacoes} onBack={handleBackToUpload} />
          )}
      </main>
    </div>
  );
}

function UploadPage({ onUpload, isLoading, error, fileName }) {
  const [file, setFile] = useState(null);

  const handleFileChange = (e) => {
    const selectedFile = e.target.files[0];
    setFile(selectedFile);
    onUpload(selectedFile);
  };

  return (
    <div className="bg-gray-800 p-8 rounded-lg shadow-2xl text-center flex flex-col items-center page-component">
      <div style={{transform: "scale(1)"}}>
        <Icon name="UploadCloud" className="text-blue-500 mx-auto mb-4" size={64} />
      </div>
      <h2 className="text-2xl font-semibold mb-2 text-gray-100">Análise de Extrato Bancário</h2>
      <p className="text-gray-400 mb-6">Envie o seu extrato em formato PDF para começar a análise.</p>
      
      <div className="relative w-full max-w-md">
        <input
          type="file"
          id="file-upload"
          className="absolute w-full h-full opacity-0 cursor-pointer custom-file-input"
          onChange={handleFileChange}
          accept=".pdf"
          disabled={isLoading}
        />
        <label htmlFor="file-upload" className={`w-full inline-block px-6 py-3 rounded-md font-semibold text-white transition-all duration-300
          ${isLoading ? 'bg-gray-600 cursor-not-allowed' : 'bg-gradient-to-r from-blue-500 to-indigo-600 hover:from-blue-600 hover:to-indigo-700 cursor-pointer'}
        `}>
          {isLoading ? 'A processar...' : 'Selecionar Extrato (PDF)'}
        </label>
      </div>

      {isLoading && (
         <div className="mt-4 flex items-center text-blue-300">
            <div className="w-5 h-5 border-t-2 border-blue-400 border-solid rounded-full animate-spin mr-2"></div>
            A analisar "{fileName}"...
         </div>
      )}
      
      {error && (
        <div className="mt-4 bg-red-900/50 text-red-300 p-4 rounded-md flex items-center text-left">
          <Icon name="AlertCircle" className="mr-3 flex-shrink-0" />
          <span>{error}</span>
        </div>
      )}
    </div>
  );
}

function DashboardPage({ resumo, transacoes, onBack }) {
  const Recharts = window.Recharts;
  const { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } = Recharts;

    if (!resumo.length && !transacoes.length) {
        return (
            <div className="text-center p-8 bg-gray-800 rounded-lg page-component">
                <Icon name="AlertCircle" className="mx-auto text-yellow-400 mb-4" size={48} />
                <h2 className="text-xl font-semibold">Nenhum dado para exibir.</h2>
                <p className="text-gray-400 mb-6">O extrato processado não continha transações reconhecidas.</p>
                <button onClick={onBack} className="mt-4 px-6 py-2 bg-blue-600 hover:bg-blue-700 text-white font-semibold rounded-md flex items-center mx-auto transition-all">
                    <Icon name="ArrowLeft" className="mr-2" size={18} /> Voltar
                </button>
            </div>
        );
    }
  return (
    <div className="space-y-8 page-component">
      <button onClick={onBack} className="mb-4 px-4 py-2 bg-gray-700 hover:bg-gray-600 text-white font-semibold rounded-md flex items-center transition-all">
        <Icon name="ArrowLeft" className="mr-2" size={18} /> Analisar Outro Extrato
      </button>

      <div className="bg-gray-800 p-6 rounded-lg shadow-2xl">
        <h2 className="text-xl font-semibold mb-4 text-gray-100 flex items-center">
            <Icon name="BarChart2" className="mr-3 text-indigo-400" /> Resumo de Despesas por Categoria
        </h2>
        <div style={{ width: '100%', height: 300 }}>
          <ResponsiveContainer>
            <BarChart data={resumo} margin={{ top: 5, right: 20, left: -10, bottom: 5 }}>
              <CartesianGrid strokeDasharray="3 3" stroke="#4a5568" />
              <XAxis dataKey="categoria" stroke="#a0aec0" />
              <YAxis stroke="#a0aec0" />
              <Tooltip cursor={{ fill: 'rgba(100, 116, 139, 0.2)' }} contentStyle={{ backgroundColor: '#1f2937', border: '1px solid #4a5568', borderRadius: '0.5rem' }} labelStyle={{ color: '#d1d5db' }}/>
              <Bar dataKey="total" name="Total Gasto" unit=" R$">
                 {resumo.map((entry, index) => (<Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />))}
              </Bar>
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
      
      <div className="bg-gray-800 p-6 rounded-lg shadow-2xl">
        <h2 className="text-xl font-semibold mb-4 text-gray-100 flex items-center">
            <Icon name="FileText" className="mr-3 text-green-400" /> Todas as Transações
        </h2>
        <div className="overflow-x-auto max-h-96">
          <table className="min-w-full text-left text-sm">
            <thead className="bg-gray-900/70 sticky top-0"><tr>
                <th className="p-3">Data</th><th className="p-3">Descrição</th><th className="p-3">Categoria</th><th className="p-3 text-right">Valor (R$)</th>
            </tr></thead>
            <tbody className="divide-y divide-gray-700">
              {transacoes.map((t) => (
                <tr key={t.id} className="hover:bg-gray-700/50">
                  <td className="p-3 whitespace-nowrap">{new Date(t.data).toLocaleDateString('pt-BR')}</td>
                  <td className="p-3">{t.descricao}</td>
                  <td className="p-3"><span className="px-2 py-1 text-xs font-semibold rounded-full bg-blue-900/50 text-blue-300">{t.categoria}</span></td>
                  <td className={`p-3 text-right font-medium whitespace-nowrap ${t.valor < 0 ? 'text-red-400' : 'text-green-400'}`}>
                    {t.valor.toFixed(2).replace('.', ',')}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

// --- Renderiza a aplicação na página ---
const container = document.getElementById('root');
const root = ReactDOM.createRoot(container);
root.render(<App />);

