#!/usr/bin/env python
from __future__ import print_function
import _ncs
import ncs.maapi as maapi
import ncs.maagic as maagic
import IPython

if __name__ == '__main__':
    m = maapi.Maapi()
    m.load_schemas()
    m.start_user_session('admin', 'system', [])
    trans = m.start_trans(_ncs.RUNNING, _ncs.READ_WRITE)

    x = maagic.get_root(trans)

    print("Your maagic object 'x -> %s' is now prepared... go have some fun!" %
          (str(x)))
    IPython.embed(display_banner=False)
