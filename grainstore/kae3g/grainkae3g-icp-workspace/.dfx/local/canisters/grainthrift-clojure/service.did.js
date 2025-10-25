export const idlFactory = ({ IDL }) => {
  return IDL.Service({
    'getHomePage' : IDL.Func([], [IDL.Text], ['query']),
    'status' : IDL.Func([], [IDL.Text], ['query']),
    'whoami' : IDL.Func([], [IDL.Text], ['query']),
  });
};
export const init = ({ IDL }) => { return []; };
