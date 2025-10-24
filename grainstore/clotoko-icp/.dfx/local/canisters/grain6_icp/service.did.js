export const idlFactory = ({ IDL }) => {
  return IDL.Service({
    'getGrainbook' : IDL.Func([IDL.Text], [IDL.Text], ['query']),
    'getGraincontacts' : IDL.Func([IDL.Text], [IDL.Text], ['query']),
    'getGraincourse' : IDL.Func([IDL.Text], [IDL.Text], ['query']),
    'getGrainpath' : IDL.Func([IDL.Text], [IDL.Text], ['query']),
    'getGraintime' : IDL.Func([IDL.Text], [IDL.Text], ['query']),
    'status' : IDL.Func([], [IDL.Text], ['query']),
    'updateGrainbook' : IDL.Func([IDL.Text], [IDL.Text], []),
    'updateGraincontacts' : IDL.Func([IDL.Text], [IDL.Text], []),
    'updateGraincourse' : IDL.Func([IDL.Text], [IDL.Text], []),
    'updateGrainpath' : IDL.Func([IDL.Text], [IDL.Text], []),
    'updateGraintime' : IDL.Func([IDL.Text], [IDL.Text], []),
    'whoami' : IDL.Func([], [IDL.Principal], ['query']),
  });
};
export const init = ({ IDL }) => { return []; };
